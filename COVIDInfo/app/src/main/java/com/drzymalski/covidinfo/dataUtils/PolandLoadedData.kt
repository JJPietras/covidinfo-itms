package com.drzymalski.covidinfo.dataUtils

import com.drzymalski.covidinfo.apiUtils.CSVManager
import com.drzymalski.covidinfo.apiUtils.models.PolandData
import com.drzymalski.covidinfo.apiUtils.models.SavedPolandData
import io.paperdb.Paper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PolandLoadedData {

    var polandData = SavedPolandData()

    init {
        try {
            loadData()
        } catch (ex: Exception) {
            loadPolandData()
        }
    }

    private fun saveData(){
        Paper.book().write("polandData.json", polandData)
    }

    private fun loadData(){
        polandData = Paper.book().read("polandData.json")
    }


    @OptIn(ExperimentalStdlibApi::class)
    fun loadPolandData(): Boolean{
        try{
            val loadedData = CSVManager.loadPolandData()
            if (loadedData.count()>0 ){
                val polandDataCSV = loadedData.first()
                if (polandDataCSV.liczba_przypadkow != null){
                    if (polandDataCSV.liczba_przypadkow!! > 0 && polandDataCSV.liczba_przypadkow != polandData.newCases){
                        polandDataCSV.liczba_przypadkow?.let {  polandData.newCases = it }
                        polandDataCSV.liczba_przypadkow?.let { polandData.dateLoaded = DateConverter.getTodayDate() }
                        polandDataCSV.liczba_przypadkow?.let { polandData.dateLoadedShort = DateConverter.getTodayDateShort() }
                        polandDataCSV.zgony?.let { polandData.died = it }
                        polandDataCSV.liczba_ozdrowiencow?.let { polandData.recovered = it }
                        saveData()
                        return true
                    }
                }
            }
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
        return false
    }

}