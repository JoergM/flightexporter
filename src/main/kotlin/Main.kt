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
        val geoAltitudes = GaugeMetricFamily("flight_geo_altitude_ft", "Altitude of the plane in feet.", listOf("callsign"))
        if (flightStates.states != null) {
            flightStates.states.forEach {
                if (it.geoAltitude != null && it.callsign != "") {
                    geoAltitudes.addMetric(listOf(it.callsign), it.geoAltitude)
                }
            }
        }
        result.add(geoAltitudes)

        return result
    }

}
