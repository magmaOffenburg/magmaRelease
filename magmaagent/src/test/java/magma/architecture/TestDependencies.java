/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "magma")
public class TestDependencies
{
	// Use this to check if a class is analyzed
	//	@ArchTest
	//	static void printClasses(JavaClasses classes)
	//	{
	//		assertTrue(classes.contain("magma.agent.decision.evaluator.impl.FallEvaluator"));
	//	}

	@ArchTest
	public static final ArchRule agentRobots = noClasses()
													   .that()
													   .resideInAPackage("..agent..")
													   .should()
													   .accessClassesThat()
													   .resideInAPackage("..robots..");

	@ArchTest
	public static final ArchRule checkLayers = layeredArchitecture()
													   .layer("decision")
													   .definedBy("..decision..")
													   .layer("model")
													   .definedBy("..model..")
													   .layer("communication")
													   .definedBy("..communication..")
													   .layer("runtime")
													   .definedBy("..agentruntime..")
													   .layer("robotsMain")
													   .definedBy("..robots")

													   .whereLayer("communication")
													   .mayOnlyBeAccessedByLayers("model", "runtime")
													   .whereLayer("model")
													   .mayOnlyBeAccessedByLayers("decision", "runtime", "robotsMain")
													   .whereLayer("decision")
													   .mayOnlyBeAccessedByLayers("runtime");
}