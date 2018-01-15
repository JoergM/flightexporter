package com.github.joergm.flightexporter

import io.prometheus.client.Collector
import io.prometheus.client.GaugeMetricFamily
import io.prometheus.client.exporter.HTTPServer
import org.opensky.api.OpenSkyApi


fun main(args: Array<String>) {
    //Register custom collector
    CustomCollector().register<CustomCollector>()

    //start httpserver
    HTTPServer(9555)

    //Loop forever (Exporter can be killed by OS Signals manually)
    while (true) {
        Thread.sleep(1000)
    }
}


class CustomCollector : Collector() {
    override fun collect(): MutableList<MetricFamilySamples> {
        val result = mutableListOf<MetricFamilySamples>()

        //read flight data, simply get all of them (they are limited to about 4500)
        val api = OpenSkyApi()
        val flightStates = api.getStates(0, null)

        //generate a labeled gauge for the altitude of all planes
        val geoAltitudes = GaugeMetricFamily("flight_geo_altitude_m", "Altitude of the plane in meter.", listOf("callsign"))
        if (flightStates.states != null) {
            flightStates.states.forEach {
                if (it.geoAltitude != null && it.callsign != "") {
                    geoAltitudes.addMetric(listOf(it.callsign.trim()), it.geoAltitude)
                }
            }
        }
        result.add(geoAltitudes)

        //generate a labeled gauge for the velocity of all planes
        val velocity = GaugeMetricFamily("flight_velocity_mph", "velocity of the plane in miles/hour.", listOf("callsign"))
        if (flightStates.states != null) {
            flightStates.states.forEach {
                if (it.velocity != null && it.callsign != "") {
                    velocity.addMetric(listOf(it.callsign.trim()), it.velocity)
                }
            }
        }
        result.add(velocity)

        return result
    }

}
