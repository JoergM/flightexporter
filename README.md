# Flightexporter

This is a small project used in a workshop to demonstrate how easy it is to create a [Prometheus](https://prometheus.io)  exporter.
It uses the publicly available API of [OpenSky](https://opensky-network.org/) to read Inflight data of airplanes (height and velocity) and publishes them in Prometheus format.

## How to use

There is a buildscript that you can use to build the exporter container locally. Start it using:

```
./buildContainer.sh
```

This builds the flightexporter image in your local docker environment. After that you can use the docker compose file:

```
docker-compose up
```

to start the exporter and a Prometheus instance. You can now open the local prometheus using (http://localhost:9090).

You will find the values created by this exporter using the names:

- flight_geo_altitude_m
- flight_velocity_mph


