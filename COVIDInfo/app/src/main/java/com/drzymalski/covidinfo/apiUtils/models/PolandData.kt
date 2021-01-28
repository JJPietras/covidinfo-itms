package com.drzymalski.covidinfo.apiUtils.models

import java.util.*

data class PolandData(var wojewodztwo: String?, var liczba_przypadkow: Int?,
                      var liczba_na_10_tys_mieszkancow: Float?, var zgony: Int?,
                      var zgony_w_wyniku_covid_bez_chorob_wspolistniejacych: Int?,
                      var zgony_w_wyniku_covid_i_chorob_wspolistniejacych: Int?,
                      var liczba_zlecen_poz: Int?, var liczba_ozdrowiencow: Int?,
                      var liczba_osob_objetych_kwarantanna: Int?,
                      var liczba_wykonanych_testow: Int?,
                      var liczba_testow_z_wynikiem_pozytywnym: Int?,
                      var liczba_testow_z_wynikiem_negatywnym: Int?,
                      var liczba_pozostalych_testow: Int?, var teryt: String?,
                      var stan_rekordu_na: String?
)
