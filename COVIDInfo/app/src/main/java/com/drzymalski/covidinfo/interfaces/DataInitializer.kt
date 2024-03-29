package com.drzymalski.covidinfo.interfaces

import com.drzymalski.covidinfo.config.ConfigurationManager
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAOptions
import com.github.aachartmodel.aainfographics.aachartcreator.AAOptionsConstructor
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aatools.AAColor

interface DataInitializer {
    val config: ConfigurationManager

    fun getChartOptions(aaChartModel: AAChartModel): AAOptions {
        val aaOptions = AAOptionsConstructor.configureChartOptions(aaChartModel)
        aaOptions.tooltip!!
            .shared(true)
            //.style(AAStyle().color(AAColor.Black))
            //.backgroundColor(AAColor.rgbaColor(200, 200, 200, 0.9f))
            /*.useHTML(true)
            .headerFormat("<small style=\\\"color: brown;\\\">{point.key}</small><table style=\\\"color: brown;\\\">")
            .pointFormat(
                "<tr><td><li></li></td><td><small>{point.series.name}: </small></td> <td><small>{point.y}</small></td></tr>"
            )
            .footerFormat("</table>")*/
            .valueDecimals(0)
        return aaOptions
    }

}