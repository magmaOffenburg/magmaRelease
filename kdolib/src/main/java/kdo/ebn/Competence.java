/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A competence module represents a behavior rule containing preconditions,
 * actions and the expected effects of rule actions execution. The runtime
 * behavior of a competence module is most importantly defined by its
 * executability (truth value of precondition) and activation, which is a
 * measure for the goal-directedness of the behavior.
 *
 * @author Klaus Dorer
 */
public class Competence extends NetworkNode implements Comparable<Competence>, INetworkConstants
{
	// structural information
	/** parameters regulating network behavior */
	protected NetworkParams networkParams;

	/** all the competence preconditions */
	protected Condition precondition;

	/** all the competence actions */
	protected List<IEBNAction> actions;

	/** all the competence effects */
	protected List<Effect> moduleEffects;

	/** all the competence resource propositions */
	private List<ResourceProposition> resources;

	// runtime information
	/** competence status: true after competence actions were executed */
	protected boolean isExecuted;

	/** difference between new and old values for the competence activation */
	protected double activationChange;

	/** executability value for the competence */
	protected double executability;

	/** gain value for the network. Used in transfer function */
	protected double gainCompetence;

	/** activation value for the competence */
	protected double mainActivation;

	/** sigma value for the network. Used in transfer function */
	protected double sigmaCompetence;

	/** value for the activation spread by successors and conflictors */
	protected transient double spreadingActivation;

	/** all kind of connections to goals (protected and non protected) */
	protected List<GoalConnection> goalConnections;

	/** all the connections to other competences (successor and conflictor) */
	protected List<CompetenceConnection> competenceConnections;

	/** used for goal tracking mechanism to avoid activation spreading */
	private IBaseGoalTrack goalTracking;

	/** the links to the resource modules */
	private List<Connection> resourceLinks;

	/**
	 * Calls constructor of parent and initialize method.
	 *
	 * @param controlName The unique name of the control which could be used as
	 *        entry point for other controls
	 */
	public Competence(final String controlName)
	{
		super(controlName);
		initialize();
	}

	/**
	 * Constructor initializing the module.
	 *
	 * @param controlName The unique name of the control which could be used as
	 *        entry point for other controls
	 * @param numberOfGoals number of goals in the whole network
	 * @param networkParams parameters from the network
	 */
	public Competence(final String controlName, final int numberOfGoals, final NetworkParams networkParams)
	{
		super(controlName);
		initialize();
		sigmaCompetence = DEFAULT_SIGMA;
		gainCompetence = DEFAULT_GAIN;
		this.networkParams = networkParams;

		if (!networkParams.getGoalTracking()) {
			goalTracking = new NoGoalTrack();
		} else {
			goalTracking = new GoalTrack(numberOfGoals);
		}
	}

	/**
	 * Initialize all attributes
	 */
	public void initialize()
	{
		mainActivation = 0.0;
		activationChange = 0.0;
		executability = 0.0;
		spreadingActivation = 0.0;
		sigmaCompetence = 0.0;
		gainCompetence = 0.0;
		isExecuted = false;
		precondition = new Condition();
		moduleEffects = new ArrayList<Effect>();
		actions = new ArrayList<IEBNAction>();
		resources = new ArrayList<ResourceProposition>();
		goalConnections = new ArrayList<GoalConnection>();
		competenceConnections = new ArrayList<CompetenceConnection>();
		resourceLinks = new ArrayList<Connection>();
	}

	/**
	 * Connects this competence with the existing competence modules. This module
	 * should not be part of the list of competences already. It is connected to
	 * a goal if this competence has an effect that is part of the goal
	 * condition. It is connected to another competence module, if it has an
	 * effect that is part of the precondition of the other module (successor) It
	 * is connected to another competence module, if it has a precondition that
	 * is an effect of the other module (predecessor)
	 *
	 * @param competences all the competences already defined in the network
	 * @param goals all the network goals
	 * @param resourceNodes all resource nodes of the network
	 * @throws NetworkConfigurationException If the network configuration cannot
	 *         be setup
	 */
	public void connectCompetence(final Iterator<Competence> competences, final Iterator<Goal> goals,
			final Iterator<Resource> resourceNodes) throws NetworkConfigurationException
	{
		connectToOtherCompetences(competences);
		connectToGoals(goals);
		connectToResources(resourceNodes);
	}

	/**
	 * Connects this competence to all other competences of the network
	 *
	 * @param competences An iterator for all other competence modules in the
	 *        network
	 */
	private void connectToOtherCompetences(final Iterator<Competence> competences)
	{
		while (competences.hasNext()) {
			Competence competence = competences.next();

			// add links from preconditions to other modules effects
			Iterator<Proposition> preconditions = getPrecondition().getPropositions();
			while (preconditions.hasNext()) {
				Proposition destination = preconditions.next();
				Iterator<Effect> otherEffects = competence.getEffects();

				while (otherEffects.hasNext()) {
					Proposition source = otherEffects.next();

					if (source.isIdentic(destination)) {
						addPredecessor(source, destination);
						competence.addSuccessor(destination, source);
					} else if (source.isInvers(destination)) {
						competence.addConflictor(destination, source);
					}
				}
			}

			// add links from effects to other modules preconditions
			Iterator<Effect> effects = getEffects();
			while (effects.hasNext()) {
				Proposition destination = effects.next();
				Iterator<Proposition> otherPreconditions = competence.getPrecondition().getPropositions();

				while (otherPreconditions.hasNext()) {
					Proposition source = otherPreconditions.next();

					if (source.isIdentic(destination)) {
						addSuccessor(source, destination);
						competence.addPredecessor(destination, source);
					} else if (source.isInvers(destination)) {
						addConflictor(source, destination);
					}
				}
			}
		}
	}

	/**
	 * Connects this competence to all goals of the network
	 *
	 * @param goals An iterator to iterate over all goals of the network
	 */
	private void connectToGoals(final Iterator<Goal> goals)
	{
		while (goals.hasNext()) {
			Goal goal = goals.next();
			Proposition source = goal.getGoalCondition();
			Iterator<Effect> effects = getEffects();

			while (effects.hasNext()) {
				Proposition destination = effects.next();
				if (source.isIdentic(destination)) {
					addGoalLink(source, destination);
				} else if (source.isInvers(destination)) {
					addProtectedGoalLink(source, destination);
				}
			}
		}
	}

	/**
	 * Connects this competence to all resources in the network
	 *
	 * @param resourceNodes An iterator to iterate over all resources of the
	 *        network
	 * @throws NetworkConfigurationException If the competence cannot be
	 *         connected to the resources
	 */
	private void connectToResources(final Iterator<Resource> resourceNodes) throws NetworkConfigurationException
	{
		for (ResourceProposition resourceProp : resources) {
			Resource resourceNode = findResourceNode(resourceProp, resourceNodes);
			ResourceLink newConnection = new ResourceLink(networkParams, resourceNode, resourceProp);
			resourceLinks.add(newConnection);
		}
	}

	/**
	 * Find a specific resource node
	 *
	 * @param resource Resource proposition to match
	 * @param resourceNodes An iterator to iterate over all resources of the
	 *        network
	 * @return The found resource
	 * @throws NetworkConfigurationException If no matching resource was found
	 */
	private Resource findResourceNode(ResourceProposition resource, Iterator<Resource> resourceNodes)
			throws NetworkConfigurationException
	{
		while (resourceNodes.hasNext()) {
			Resource result = resourceNodes.next();
			if (result.getName().equalsIgnoreCase(resource.getName())) {
				return result;
			}
		}
		throw new NetworkConfigurationException("Resource not existing: " + resource);
	}

	/**
	 * Calculates activation and executability and decides if this node should be
	 * executed
	 */
	@Override
	public void perform()
	{
		final double activity = getActivationAndExecutability();
		setExecuted(false);
		if (activity < 0.01) {
			// the module is not executable
			return;
		}

		// check if this module may be executed
		for (Connection resourceLink : resourceLinks) {
			Resource resource = (Resource) resourceLink.getSourceModule();

			if (resource.isActivityLowerThanThreshold(activity)) {
				// we do not have enough activity to claim resource
				return;
			}

			int amountUsed = ((ResourceProposition) resourceLink.getDestinationProposition()).getAmountUsed();

			if (amountUsed > resource.getAmountAvailable()) {
				// there is not enough resource amount for us
				return;
			}
		}

		// execute module
		try {
			for (IEBNAction action : actions) {
				action.perform();
			}

			setExecuted(true);
		} catch (Exception ex) {
			System.out.println("Error executing module: " + ex);
		}

		// inform resources about execution
		for (Connection resourceLink : resourceLinks) {
			Resource resource = (Resource) resourceLink.getSourceModule();
			int amountUsed = ((ResourceProposition) resourceLink.getDestinationProposition()).getAmountUsed();
			resource.reduceAmountAvailable(amountUsed);
		}
	}

	/**
	 * Calculates the new activation of a competence module based on the previous
	 * activation and new activation/inhibition from goals and other competence
	 * modules
	 *
	 * @return Calculated activation
	 */
	public double calculateActivation()
	{
		final double previousActivation = calculatePreviousActivation();

		calculateExternActivation();
		calculateSpreadingActivation();

		goalTracking.setToNewActivation();
		spreadingActivation = previousActivation + goalTracking.getSumActivation();
		return spreadingActivation;
	}

	/**
	 * Calculates the part of activation that is taken over from the previous
	 * activation by multiplying it with beta.
	 * @return the activation multiplied by beta
	 */
	protected double calculatePreviousActivation()
	{
		return mainActivation * networkParams.getBeta();
	}

	/**
	 * Calculates activation of this module spread by the goals
	 */
	protected void calculateExternActivation()
	{
		Iterator<GoalConnection> connections = getGoalConnections();
		while (connections.hasNext()) {
			GoalConnection connection = connections.next();
			double activation = connection.getActivation();
			int i = connection.getGoalIndex();
			goalTracking.setActivation(i, activation);
		}
	}

	/**
	 * Calculates the activation of this module spread by the other competences
	 */
	protected void calculateSpreadingActivation()
	{
		Iterator<CompetenceConnection> connections = getCompetenceConnections();
		while (connections.hasNext()) {
			CompetenceConnection connection = connections.next();

			int noOfGoals = goalTracking.getNoOfGoals();
			for (int i = 0; i < noOfGoals; i++) {
				goalTracking.setActivation(i, connection.getGoalActivation(i));
			}
		}
	}

	/**
	 * Calculates competence executability number as a product of all the truth
	 * values of the competence preconditions.
	 *
	 * @return Calculated executability
	 */
	public double calculateExecutability()
	{
		executability = precondition.getTruthValue();
		return executability;
	}

	/**
	 * Calculate a combination of activation and executability (in this
	 * implementation it is the product of both)
	 *
	 * @return The calculated combination
	 */
	public double getActivationAndExecutability()
	{
		return getExecutability() * getActivation();
	}

	/**
	 * Adds an activation link from a goal
	 *
	 * @param source the proposition that is the source for activation
	 * @param destination the proposition (within this module) that is the
	 *        destination of activation
	 */
	protected void addGoalLink(final Proposition source, final Proposition destination)
	{
		assert destination.getContainingNode() ==
				this : "Destination of goal link should be this module, but is: " + destination.getContainingNode() +
					   " destination: " + destination + " this: " + this;

		addGoalConnection(new GoalLink(networkParams, source, destination));
	}

	/**
	 * Adds an inhibition link from a goal
	 *
	 * @param source the proposition that is the source for activation
	 * @param destination the proposition (within this module) that is the
	 *        destination of activation
	 */
	protected void addProtectedGoalLink(final Proposition source, final Proposition destination)
	{
		assert (destination.getContainingNode() == this)
			: "Destination of protected goal link is not this: " + destination.getContainingNode() +
			  " destination: " + destination + " this: " + this;

		addGoalConnection(new ProtectedGoalLink(networkParams, source, destination));
	}

	/**
	 * Adds the passed connection to the list of goal connections
	 *
	 * @param newConnection the goal connection to add
	 */
	protected void addGoalConnection(GoalConnection newConnection)
	{
		goalConnections.add(newConnection);
	}

	/**
	 * Adds an activation link from a successor module (module that has a
	 * precondition we can fulfill)
	 *
	 * @param source the proposition that is the source for activation
	 * @param destination the proposition (within this module) that is the
	 *        destination of activation
	 */
	protected void addSuccessor(final Proposition source, final Proposition destination)
	{
		assert (destination.getContainingNode() == this)
			: "Destination of successor link is not this: " + destination.getContainingNode() +
			  " destination: " + destination + " this: " + this;

		addCompetenceConnection(new SuccessorLink(networkParams, source, destination));
	}

	/**
	 * Adds a link from a predecessor module (this implementation is empty and
	 * does nothing)
	 *
	 * @param source the proposition that is the source for activation
	 * @param destination the proposition (within this module) that is the
	 *        destination of activation
	 */
	protected void addPredecessor(final Proposition source, final Proposition destination)
	{
		// empty, not used in this version of EBNs
	}

	/**
	 * Adds an inhibition link from another competence module
	 *
	 * @param source the proposition that is the source for activation
	 * @param destination the proposition (within this module) that is the
	 *        destination of activation
	 */
	protected void addConflictor(final Proposition source, final Proposition destination)
	{
		assert (destination.getContainingNode() == this)
			: "Destination of conflictor link is not this: " + destination.getContainingNode() +
			  " destination: " + destination + " this: " + this;

		addCompetenceConnection(new ConflictorLink(networkParams, source, destination));
	}

	/**
	 * Adds the passed connection to the list of competence connections
	 *
	 * @param newConnection the competence connection to add
	 */
	protected void addCompetenceConnection(CompetenceConnection newConnection)
	{
		competenceConnections.add(newConnection);
	}

	/**
	 * Adds a resource to the list of resources of this competence module
	 *
	 * @param resource a resource proposition to add to this competence module
	 */
	public void addResource(final ResourceProposition resource)
	{
		resources.add(resource);
	}

	/**
	 * Calculate the activation of the goaltracking passed through the transfer
	 * function
	 *
	 * @param goalIndex the index of the goal for which we want to know incoming
	 *        activation (direct or indirect)
	 * @return Calculated activation
	 */
	public double getTransferedActivation(final int goalIndex)
	{
		return transfer(goalTracking.getActivation(goalIndex));
	}

	/**
	 * Sets new value to networkParams attribute
	 *
	 * @param params the network parameters that define the runtime behavior of
	 *        the network
	 */
	public void setNetworkParams(final NetworkParams params)
	{
		networkParams = params;
		sigmaCompetence = params.getSigma();
		gainCompetence = params.getGain();
	}

	/**
	 * The transfer function defines the mapping from incoming activation to the
	 * module's new activation. Depending on the NO_TRANSFER_FUNCTION this is
	 * either just returning the passed value or using a sigmoidal transfer
	 * function: 1 / 1 + e ^ (m * (s - x))
	 *
	 * @param value the value to be transfered
	 * @return the result of the transfer function
	 */
	private double transfer(final double value)
	{
		if (networkParams.getTransferFunction()) {
			return value;
		}

		return 1.0 / (1.0 + Math.exp(gainCompetence * (sigmaCompetence - value)));
	}

	/**
	 * Calculates the activation change and sets the new activation
	 * @param activation the new activation of this module
	 */
	private void setActivation(final double activation)
	{
		activationChange = activation - mainActivation;
		mainActivation = activation;
	}

	/**
	 * Sets the new activation of this module. Calculate activation and set
	 * activation are separate methods to avoid that the order of calculating
	 * activation has influence on the calculation.
	 */
	public void setToNewActivation()
	{
		setActivation(spreadingActivation);
	}

	/**
	 * Adds a precondition to the list of preconditions of this competence module
	 * (which are assumed to be connected by logical AND)
	 *
	 * @param proposition the proposition to be added
	 */
	public void addPrecondition(final Proposition proposition)
	{
		proposition.setContainingNode(this);
		precondition.addProposition(proposition);
	}

	/**
	 * Adds an effect to the list of effects of this competence module (which are
	 * assumed to be connected by logical AND)
	 *
	 * @param proposition the proposition to be added
	 */
	public void addEffect(final Effect proposition)
	{
		proposition.setContainingNode(this);
		moduleEffects.add(proposition);
	}

	/**
	 * Adds an action to the list of actions of this competence module (all
	 * actions are executed)
	 *
	 * @param action the action to be added
	 */
	public void addAction(final IEBNAction action)
	{
		actions.add(action);
	}

	/**
	 * Retrieve an iterator for the list of preconditions
	 *
	 * @return Iterator
	 */
	public Condition getPrecondition()
	{
		return precondition;
	}

	/**
	 * Retrieve an iterator for the list of effects
	 *
	 * @return Iterator
	 */
	public Iterator<Effect> getEffects()
	{
		return moduleEffects.iterator();
	}

	/**
	 * Retrieve an iterator for the list of actions
	 *
	 * @return Iterator
	 */
	public Iterator<IEBNAction> getActions()
	{
		return actions.iterator();
	}

	/**
	 * Retrieve an iterator over all resource prepositions
	 *
	 * @return Iterator
	 */
	public Iterator<ResourceProposition> getResourcePropositions()
	{
		return resources.iterator();
	}

	/**
	 * Retrieve an iterator for the list of all connections to goals
	 *
	 * @return Iterator
	 */
	protected Iterator<GoalConnection> getGoalConnections()
	{
		return goalConnections.iterator();
	}

	/**
	 * Retrieve an iterator for the list of all connections to other competences
	 *
	 * @return Iterator
	 */
	protected Iterator<CompetenceConnection> getCompetenceConnections()
	{
		return competenceConnections.iterator();
	}

	/**
	 * Retrieve an iterator for the list of all connections to resource nodes
	 *
	 * @return Iterator
	 */
	protected Iterator<Connection> getResourceConnections()
	{
		return resourceLinks.iterator();
	}

	/**
	 * Retrieve the activation value of this module
	 *
	 * @return Activation
	 */
	@Override
	public double getActivation()
	{
		return mainActivation;
	}

	/**
	 * Retrieve the executability of this module as a truth value of the
	 * precondition
	 * @return Executability [0..1]
	 */
	public double getExecutability()
	{
		return executability;
	}

	/**
	 * Retrieve the main activation of this module. In the default implementation
	 * the activation will be returned.
	 *
	 * @return Main activation
	 */
	public double getMainActivation()
	{
		return mainActivation;
	}

	/**
	 * Check if this module was executed in the last action selection cycle
	 *
	 * @return True if it was exectuted, false if not
	 */
	public boolean isExecuted()
	{
		return isExecuted;
	}

	/**
	 * Set if this module was executed
	 *
	 * @param value New executed state
	 */
	protected void setExecuted(boolean value)
	{
		isExecuted = value;
	}

	/**
	 * Returns the goal track.
	 *
	 * @return IBaseGoalTrack The goalTracking value.
	 */
	public IBaseGoalTrack getGoalTracking()
	{
		return goalTracking;
	}

	/**
	 * Serialize this object into a string (with indentation)
	 *
	 * @param tabs The number of tabs used to make indentation in the output
	 * @return A string representation of this object
	 */
	@Override
	public String toString(final int tabs)
	{
		final StringBuffer info = new StringBuffer(200);
		StringBuffer tempBuffer = new StringBuffer(200);

		tempBuffer.append(precondition.toString(tabs));
		if (tempBuffer.length() > 0) {
			info.append(PrintUtil.addTabs(tabs))
					.append("Preconditions: ")
					.append(PrintUtil.addTabs(tabs + 1))
					.append(tempBuffer);
			tempBuffer = new StringBuffer(200);
		}

		for (IEBNAction action : actions) {
			if (tempBuffer.length() > 0) {
				tempBuffer.append(", ");
			}
			tempBuffer.append(action.getName());
		}

		if (tempBuffer.length() > 0) {
			info.append(PrintUtil.addTabs(tabs))
					.append("Actions: ")
					.append(PrintUtil.addTabs(tabs + 1))
					.append(tempBuffer);
			tempBuffer = new StringBuffer(200);
		}

		Iterator<Effect> effects = getEffects();
		while (effects.hasNext()) {
			if (tempBuffer.length() > 0) {
				tempBuffer.append(", ");
			}
			tempBuffer.append((effects.next()).toString());
		}

		if (tempBuffer.length() > 0) {
			info.append(PrintUtil.addTabs(tabs))
					.append("Effects: ")
					.append(PrintUtil.addTabs(tabs + 1))
					.append(tempBuffer);
			tempBuffer = new StringBuffer(200);
		}

		for (ResourceProposition resource : resources) {
			if (tempBuffer.length() > 0) {
				tempBuffer.append(", ");
			}
			tempBuffer.append((resource).toString());
		}

		if (tempBuffer.length() > 0) {
			info.append(PrintUtil.addTabs(tabs))
					.append("Resources: ")
					.append(PrintUtil.addTabs(tabs + 1))
					.append(tempBuffer);
		}

		info.append(PrintUtil.addTabs(tabs))
				.append("current activation: ")
				.append(getActivation())
				.append(PrintUtil.addTabs(tabs))
				.append("current executability: ")
				.append(getExecutability());

		return info.toString();
	}

	/**
	 * Implementation of the Comparable interface method. Compares the h-value
	 * (executability and activation) of the two values in order to allow to sort
	 * the modules due to their utility.
	 *
	 * @param other The competence module to compare to
	 * @return -1 if h-value of this object is less then that of other, 1 if
	 *         h_value of this object is bigger then that of other, 0 otherwise
	 */
	@Override
	public int compareTo(Competence other)
	{
		final double value1 = getActivationAndExecutability();
		final double value2 = other.getActivationAndExecutability();

		if (value1 < value2) {
			return -1;
		} else if (value1 > value2) {
			return 1;
		} else {
			return 0;
		}
	}
}
