## Docker Container for rcsserver3d

### Starting the container for the first time
1. Install [Docker](https://www.docker.com/get-started)
2. Start Docker
3. Open terminal at agent/util/docker/rcssserver3d (this folder)
4. `docker build -t simspark .`
5. `docker run -d -p 3100:3100 -p 3200:3200 --name simspark simspark`
6. *Using the server for simulations*
7. `docker stop simspark`

### Starting the container after the first time 
1. Start Docker
2. Open terminal at agent/util/docker/rcssserver3d (this folder)
3. `docker start simspark`
4. *Using the server for simulations*
5. `docker stop simspark`