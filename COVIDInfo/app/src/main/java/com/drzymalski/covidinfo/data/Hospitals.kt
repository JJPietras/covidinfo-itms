package com.drzymalski.covidinfo.data

import com.drzymalski.covidinfo.lib.Hospital

object Hospitals {
    val regionHospitals = mapOf(

        "dolnośląskie" to listOf(

            Hospital(
                "Specjalistyczny szpital",
                "dra Alfreda Sokołowskiego",
                "Wałbrzych",
                "Batorego 4, (74) 648 98 06",
                arrayOf(50.774801287843914, 16.277523078788604)
            ),

            Hospital(
                "Uniwersytecki Szpital Kliniczny",
                "Jana Mikulicza-Radeckiego",
                "Wrocław - Śródmieście",
                "Chałubińskiego 2-2A, (71) 770 31 51",
                arrayOf(51.110050118619625, 17.067400440245894)
            ),

            Hospital(
                "Wojewódzki Szpital Specjalistyczny",
                " Jerzego Gromkowskiego",
                "Wrocław - Psie Pole",
                "Koszarowa 5, kom. 519 338 486",
                arrayOf(51.14205036860373, 17.066190380413932)
            ),

            Hospital(
                "Zespół Opieki Zdrowotnej",
                "",
                "Bolesławiec",
                "ul. Jeleniogórska 4, (75) 738 01 20",
                arrayOf(51.25494661328385, 15.567085668164227)
            )
        ),

        "kujawsko-pomorskie" to listOf(

            Hospital(
                "Wojewódzki Szpital Obserwacyjno-Zakaźny",
                "Tadeusza Browicza",
                "Bydgoszcz",
                "Św. Floriana 12, (52) 325 56 34, (52)325 56 03",
                arrayOf(53.12213748430281, 18.00920471039974)
            ),

            Hospital(
                "Regionalny Szpital Specjalistyczny",
                "dra Władysława Biegańskiego",
                "Grudziądz",
                "L. Rydygiera 15-17, (56) 641 44 44",
                arrayOf(53.4525213189633, 18.786528084803624)
            ),

            Hospital(
                "Wojewódzki Szpital Zespolony",
                "Ludwika Rydygiera",
                "Toruń",
                "Krasińskiego 4/4a, (56) 679 55 27",
                arrayOf(53.010845661374255, 18.59485803052856)
            ),

            Hospital(
                "NZOZ „Nowy Szpital Sp. z o.o.”",
                "Prowadzony przez Nowy Szpital Sp. z o.o.",
                "Świecie",
                "Wojska Polskiego 126, (41) 240 16 32",
                arrayOf(53.41377878367128, 18.46677802186013)
            )
        ),

        "lubelskie" to listOf(

            Hospital(
                "Arion Szpitale Sp. z o.o.",
                "",
                "Biłgoraj",
                "Pojaska 5, (84) 688 22 55",
                arrayOf(50.52951822638007, 22.713710147477943)
            ),

            Hospital(
                "Samodzielny Nr 1",
                "",
                "Lublin",
                "Staszica 16, (81) 534 94 16",
                arrayOf(51.25088570008131, 22.5637035892695)
            ),

            Hospital(
                "Samodzielny Publiczny Szpital Wojewódzki",
                "Jana Bożego",
                "Lublin",
                "Mieczysława Biernackiego 9, (81) 740 42 75",
                arrayOf(51.25625093042242, 22.566827842250607)
            ),

            Hospital(
                "Samodzielny Publiczny Wojewódzki Szpital Specjalistyczny",
                "",
                "Chełm",
                "Szpitalna 53b/Paw.B, (82) 562 33 61 ",
                arrayOf(51.14928645015245, 23.45659894360303)
            ),

            Hospital(
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "",
                "Łuków",
                "dra Andrzeja Rogalińskiego 3, (25) 798 20 01 wew. 292",
                arrayOf(51.9243004747742, 22.379353832585497)
            ),

            Hospital(
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "",
                "Puławy",
                "Józefa Bema 1, (81) 450 22 77",
                arrayOf(51.42181959758391, 21.97444438318613)
            ),

            Hospital(
                "Samodzielny Publiczny Zespół Opieki Zdrowotnej",
                "",
                "Tomaszów Lubelski",
                "Al. Grunwaldzkie 1, (84) 664 44 11",
                arrayOf(50.443067383945504, 23.416041998352984)
            ),

            Hospital(
                "Wojewódzki Szpital Specjalistyczny",
                "",
                "Biała Podlaska",
                "Terebelska 57-65, (83) 414 73 16, (83) 414 71 16",
                arrayOf(52.04669264023369, 23.111717989430034)
            )
        ),

        "lubuskie" to listOf(

            Hospital(
                "Szpital Uniwersytecki",
                "K. Marcinkowskiego",
                "Zielona Góra",
                "Zyty 26, (68) 329 64 78, (68) 329 64 81",
                arrayOf(51.94170981667916, 15.518497933244502)
            ),

            Hospital(
                "Wielospecjalistyczny Szpital Wojewódzki Sp.z o.o.",
                "",
                "Gorzów Wielkopolski",
                "Walczaka 42",
                arrayOf(52.75329426312344, 15.259333641556028)
            )
        ),

        "łódzkie" to listOf(

            Hospital(
                "Szpital Powiatowy",
                "",
                "Radomsko",
                "ul. Jagiellońska 36, (44) 681 08 31",
                arrayOf(51.05823048765563, 19.45795853793071)
            ),

            Hospital(
                "Szpital Wojewódzki",
                "Jana Pawła II",
                "Bełchatów",
                "Czapliniecka 123, kom. 512 030 463",
                arrayOf(51.37498965927504, 19.35032963788928)
            ),

            Hospital(
                "Tomaszowskie Centrum Zdrowia",
                "",
                "Tomaszów Mazowiecki",
                "Jana Pawła II 35, (44) 725 72 09",
                arrayOf(51.52301552198138, 20.004894497462306)
            ),

            Hospital(
                "Wojewódzki Specjalistyczny Szpital",
                "dra Wł. Biegańskiego",
                "Łódź - Bałuty",
                "Gen. Kniaziewicza 1/5, kom. 887 877 690",
                arrayOf(51.80082871686273, 19.437372362383353)
            ),

            Hospital(
                "Wojewódzki Szpital Specjalistyczny",
                "M. Skłodowskiej-Curie",
                "Zgierz",
                "Parzęczewska 35, (42) 714 45 00",
                arrayOf(51.862932203998454, 19.38849579037835)
            )
        ),

        "małopolskie" to listOf(

            Hospital(
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "Szpital Uniwersytecki",
                "Kraków",
                "Macieja Jakubowskiego 2, (12) 400 20 00",
                arrayOf(50.01061136263048, 20.00133038200348)
            ),

            Hospital(
                "Szpital Specjalistyczny",
                "Stefana Żeromskiego",
                "Kraków",
                "os. Na Skarpie 66, (12) 622 94 03",
                arrayOf(50.06574769577447, 20.04661297554352)
            ),

            Hospital(
                "Szpital Specjalistyczny",
                "Jana Pawła II",
                "Kraków",
                "Prądnicka 80, (12) 614 23 23",
                arrayOf(50.09054318501235, 19.93719830695038)
            ),

            Hospital(
                "Nowy Szpital w Olkuszu Sp. Z o.o.",
                "",
                "Olkusz",
                "1000-Lecia 13, (41) 240 12 72",
                arrayOf(50.26897303888583, 19.568644937697663)
            ),

            Hospital(
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "",
                "Myślenice",
                "Szpitalna 2, (12) 273 03 54",
                arrayOf(49.82902038244312, 19.9463641057499)
            ),

            Hospital(
                "Specjalistyczny Szpital",
                "E. Szczeklika",
                "Tarnów",
                "Szpitalna 13, (14) 631 03 92",
                arrayOf(50.017348544496734, 20.994797279025686)
            ),

            Hospital(
                "Szpital Specjalistyczny",
                "Jędrzeja Śniadeckiego",
                "Wielogłowy",
                "Dąbrowa 1, (18) 433 21 23",
                arrayOf(49.678991597202184, 20.678377752557882)
            ),

            Hospital(
                "Szpital",
                "Św. Anny",
                "Miechów",
                "Szpitalna 3, (41) 382 03 64",
                arrayOf(50.35344291985094, 20.034828915665763)
            ),

            Hospital(
                "Zespół Opieki Zdrowotnej",
                "",
                "Dąbrowa Tarnowska",
                "Szpitalna 1, (14) 644 32 88",
                arrayOf(50.17688684479699, 20.975500817671)
            ),

            Hospital(
                "Samodzielny Publiczny Zespół Opieki Zdrowotnej",
                "",
                "Proszowice",
                "Kopernika 13, (12) 386 52 10",
                arrayOf(50.185618099791185, 20.299047331770737)
            )
        ),

        "mazowieckie" to listOf(

            Hospital(
                "Mazowiecki Szpital Specjalistyczny",
                "dra Józef Psarskiego",
                "Ostrołęka",
                "al. Jana Pawła II 120A, (29) 765 12 63",
                arrayOf(53.06311894751158, 21.589697022092675)
            ),

            Hospital(
                "Radomski Szpital Specjalistyczny",
                "dra Tytusa Chałubińskiego",
                "Radom",
                "ul. Tochtermana 1, (48) 361 52 20",
                arrayOf(51.39980263304831, 21.14587040228191)
            ),

            Hospital(
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "",
                "Siedlce",
                "Starowiejska 15, (25) 632 20 61",
                arrayOf(52.1684989124801, 22.285748172724535)
            ),

            Hospital(
                "Samodzielny Publiczny Zespół Zakładów Opieki Zdrowotnej",
                "",
                "Kozienice",
                "Al. W. Sikorskiego 10, (48) 679 72 89",
                arrayOf(51.59196323027241, 21.524861183600883)
            ),

            Hospital(
                "Specjalistyczny Szpital Wojewódzki",
                "",
                "Ciechanów",
                "Powstańców Wielkopolskich 2, (23) 673 02 77",
                arrayOf(52.883657864993594, 20.635322912817713)
            ),

            Hospital(
                "Wojewódzki Szpital Zespolony",
                "",
                "Płock",
                "Medyczna 19, (24) 364 68 71, (24) 364 64 03",
                arrayOf(52.556951325885706, 19.653966335115115)
            ),

            Hospital(
                "Wojewódzki Szpital Zakaźny",
                "",
                "Warszawa",
                "Wolska 37, (22) 335 52 61",
                arrayOf(52.23441128280649, 20.971928672588273)
            ),

            Hospital(
                "Wojskowy Instytut Medyczny",
                "",
                "Warszawa",
                "Szaserów 128, 261 817 519, 810 80 89",
                arrayOf(52.25104804069693, 21.08854482368829)
            ),

            Hospital(
                "Centralny Szpital Kliniczny MSWiA",
                "",
                "Warszawa",
                "Wołoska 137, (22) 508 20 00",
                arrayOf(52.19865265749365, 20.999350250418694)
            ),

            Hospital(
                "Centrum Kliniczne Warszawskiego Uniwersytetu Medycznego",
                "Józefa Polikarpa Brudzińskiego",
                "Warszawa – Ochota",
                "Żwirki i Wigury 63A, (22) 317 92 31",
                arrayOf(52.20715004724628, 20.98318523972779)
            )
        ),

        "opolskie" to listOf(

            Hospital(
                "Samodzielny Publiczny Zespół Opieki Zdrowotnej",
                "",
                "Kędzierzyn-Koźle",
                "24 Kwietnia 5, (77) 406 24 82",
                arrayOf(50.33209981491282, 18.149746562083717)
            ),

            Hospital(
                "Szpital Wojewódzki w Opolu Sp. z o.o.",
                "",
                "Opole",
                "Kośnego 53, (77) 443 30 43",
                arrayOf(50.670814276386245, 17.936380018353272)
            ),

            Hospital(
                "Oddział Obserwacyjno-Zakaźny",
                "Zespół Opieki Zdrowotnej",
                "Nysa",
                "Bohaterów Warszawy 23, (77) 408 79 56",
                arrayOf(50.472835825547776, 17.327152185407787)
            )
        ),

        "podkarpackie" to listOf(

            Hospital(
                "Centrum Medyczne w Łańcucie Sp. z o. o.",
                "",
                "Łańcut",
                "Ignacego Paderewskiego 5, (17) 224 02 92",
                arrayOf(50.06628756363313, 22.23302984864349)
            ),

            Hospital(
                "Centrum Opieki Medycznej",
                "",
                "Jarosław",
                "3 Maja 70, (16) 624 51 19",
                arrayOf(50.01287541249297, 22.693416666765206)
            ),

            Hospital(
                "Samodzielny Publiczny Zespół Opieki Zdrowotnej",
                "",
                "Sanok",
                "800-lecia 26, (13) 465 62 24",
                arrayOf(49.5519123807296, 22.19878404926881)
            ),

            Hospital(
                "Szpital Specjalistyczny",
                "Edmunda Biernackiego",
                "Mielec",
                "Żeromskiego 22, (17) 780 03 90",
                arrayOf(50.28824685439954, 21.437351506914922)
            ),

            Hospital(
                "Szpital Specjalistyczny",
                "",
                "Jasło",
                "Lwowska 22, (13) 443 75 29",
                arrayOf(49.75204304175195, 21.48044928850725)
            ),

            Hospital(
                "Wojewódzki Szpital",
                "Św. Ojca Pio",
                "Przemyśl",
                "Rogozińskiego 30, (16) 670 61 07",
                arrayOf(49.809072152381646, 22.782929049719417)
            ),

            Hospital(
                "Zespół Opieki Zdrowotnej",
                "",
                "Dębica",
                "Krakowska 91, (14) 680 83 87",
                arrayOf(50.038919665090305, 21.393222038968876)
            )
        ),

        "podlaskie" to listOf(

            Hospital(
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "",
                "Augustów",
                "Szpitalna 12, (87) 644 43 48, (87) 644 42 00",
                arrayOf(53.8515138494795, 22.993449872424815)
            ),

            Hospital(
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "",
                "Bielsk Podlaski",
                "Kleszczelowskiego 1, (85) 833 43 02",
                arrayOf(52.75943205459083, 23.20262670230429)
            ),

            Hospital(
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "",
                "Hajnówka",
                "doc. Adama Dowgirda 9, kom. 606 996 889",
                arrayOf(52.74654305833264, 23.61023352700306)
            ),

            Hospital(
                "Szpital Ogólny",
                "dra Witolda Ginela",
                "Grajewo",
                "Konstytucji 3 Maja 34, (86) 272 32 71 wew. 233",
                arrayOf(53.645811839072074, 22.470098878286937)
            ),

            Hospital(
                "Szpital Wojewódzki",
                "dra Ludwika Rydygiera",
                "Suwałki",
                "Szpitalna 60, (87) 562 92 15",
                arrayOf(54.1195125035075, 22.925390503119925)
            ),

            Hospital(
                "Szpital Wojewódzki",
                "Kardynała Stefana Wyszyńskiego",
                "Łomża",
                "Al. Marsz. Józefa Piłsudskiego 11, (85) 473 33 58",
                arrayOf(53.1601596811027, 22.085468043226992)
            ),

            Hospital(
                "Uniwersytecki Dziecięcy Szpital Kliniczny",
                "Ludwika Zamenhofa",
                "Białystok",
                "Waszyngtona 17, (85) 745 06 93",
                arrayOf(53.123858177726, 23.155783235871294)
            ),

            Hospital(
                "Uniwersytecki Szpital Kliniczny",
                "",
                "Białystok",
                "Żurawia 14, (85) 740 95 73",
                arrayOf(53.10874621173284, 23.190521370650227)
            )
        ),

        "pomorskie" to listOf(

            Hospital(
                "Uniwersyteckie Centrum Medycyny Morskiej i Tropikalnej",
                "",
                "Gdynia",
                "Powstania Styczniowego 9B",
                arrayOf(54.48815801250013, 18.549286769974493)
            ),

            Hospital(
                "7 Szpital Marynarki Wojennej z Przychodnią SPZOZ",
                "kontradm. prof. W. Łasińskiego",
                "Gdańsk",
                "Polanki 117 (58) 552 63 18",
                arrayOf(54.39391694058237, 18.566748962815005)
            ),

            Hospital(
                "Pomorskie Centrum Chorób Zakaźnych i Gruźlicy",
                "",
                "Gdańsk",
                "Mariana Smoluchowskiego 18, (58) 341 55 47",
                arrayOf(54.36423651556871, 18.616929170685648)
            ),

            Hospital(
                "Szpital Specjalistyczny w Kościerzynie Sp. zo.o.",
                "",
                "Kościerzyna",
                "Alojzego Piechowskiego 36, (58) 686 01 01",
                arrayOf(54.1213916893964, 17.949606603144154)
            ),

            Hospital(
                "Szpitale Pomorskie Sp z o.o. Szpital Specjalistyczny",
                "F. Ceynowy",
                "Wejherowo",
                "dr. Alojzego Jagalskiego 10",
                arrayOf(54.61473506830972, 18.24492814258252)
            )
        ),

        "śląskie" to listOf(

            Hospital(
                "Megrez Sp. z o. o.",
                "",
                "Tychy",
                "Edukacji 102, (32) 325 53 04",
                arrayOf(50.113849000306374, 19.00763224011235)
            ),

            Hospital(
                "Szpital Powiatowy",
                "",
                "Zawiercie",
                "Miodowa 14, (32) 674 02 89",
                arrayOf(50.48174432459261, 19.408849987770417)
            ),

            Hospital(
                "Szpital Rejonowy",
                "dr Józefa Rostka",
                "Racibórz",
                "Gamowska 3, (32) 755 53 72",
                arrayOf(50.1013432194554, 18.203444520446954)
            ),

            Hospital(
                "Szpital Specjalistyczny nr 1",
                "",
                "Bytom",
                "al. Legionów 49, (32) 281 92 41 wew. 230",
                arrayOf(50.35371320545256, 18.911833105972025)
            ),

            Hospital(
                "Szpital Specjalistyczny",
                "",
                "Chorzów",
                "Zjednoczenia 10, 797 189 603",
                arrayOf(50.29189521614634, 18.953130063149125)
            ),

            Hospital(
                "Wojewódzki Szpital Specjalistyczny im. N.M.P.",
                "",
                "Częstochowa",
                "Polskiego Czerwonego Krzyża 7, (34) 367 38 72",
                arrayOf(50.830409529992856, 19.110997600407508)
            ),

            Hospital(
                "Zespół Zakładów Opieki Zdrowotnej",
                "",
                "Cieszyn",
                "Bielska 4, (32) 854 92 00 wew. 492",
                arrayOf(49.74469387185756, 18.641268631238017)
            )
        ),

        "świętokrzyskie" to listOf(

            Hospital(
                "Powiatowy Zakład Opieki Zdrowotnej",
                "",
                "Starachowice",
                "Radomska 70, (41) 273 91 45",
                arrayOf(51.06478352906668, 21.072635938503417)
            ),

            Hospital(
                "Wojewódzki Szpital Zespolony",
                "",
                "Kielce",
                "Radiowa 7, (41) 363 71 33",
                arrayOf(50.87489538114561, 20.637532989106433)
            ),

            Hospital(
                "Zespół Opieki Zdrowotnej",
                "",
                "Busko-Zdrój",
                "Bohaterów Warszawy 67, (41) 378 24 01",
                arrayOf(50.46774674783712, 20.704030503498984)
            ),

            Hospital(
                "Wojewódzki Szpital Zaspolony",
                "Oddział Zakaźny Dziecięcy",
                "Kielce",
                "Grunwaldzka 45, (41) 303 32 30",
                arrayOf(50.87372737129039, 20.601186355620644)
            ),

            Hospital(
                "Świętokrzyskie Centrum Pediatrii",
                "",
                "Kielce",
                "Radiowa 7, (41) 363 71 33",
                arrayOf(50.87323278637089, 20.599630136713436)
            )
        ),

        "warmińsko-mazurskie" to listOf(

            Hospital(
                "„Szpital Giżycki” Sp. z o. o.",
                "",
                "Giżycko",
                "Warszawska 41, (87) 429 66 91",
                arrayOf(54.037924674421106, 21.78230940143359)
            ),

            Hospital(
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "Szpital Powiatowy",
                "",
                "Sienkiewicza 2, (87) 425 45 65",
                arrayOf(53.62602048761202, 21.799524302838858)
            ),

            Hospital(
                "Szpital w Ostródzie S.A.",
                "",
                "Ostróda",
                "Jagiełły 1, (89) 646 06 47 (lekarze)",
                arrayOf(53.692945327494265, 19.974660159060626)
            ),

            Hospital(
                "Szpital miejski",
                "św. Jana Pawła II",
                "Elbląg",
                "Żeromskiego 22, (55) 230 42 58",
                arrayOf(54.157315132761426, 19.422090052710924)
            ),

            Hospital(
                "Wojewódzki Specjalistyczny Szpital Dziecięcy",
                "prof. dr Stanisława Popowskiego",
                "Olsztyn",
                "Żołnierska 18A, (89) 539 34 21",
                arrayOf(53.76846894961573, 20.495398521480904)
            )
        ),

        "wielkopolskie" to listOf(

            Hospital(
                "Specjalistyczny Zespół Opieki Zdrowotnej nad Matką i Dzieckiem",
                "",
                "Poznań - Stare Miasto",
                "Nowowiejskiego 56/58, (61) 859 03 36",
                arrayOf(52.41506460467537, 16.920283008342228)
            ),

            Hospital(
                "Szpital kliniczny",
                "Karola Jonschera",
                "Poznań - Jeżyce",
                "Szpitalna 27/33, kom. 504 907 994",
                arrayOf(52.41203203768499, 16.87777975781675)
            ),

            Hospital(
                "Wielospecjalistyczny Szpital Miejski",
                "Józefa Strusia",
                "Poznań - Nowe Miasto",
                "Szwajcarska 3, (61) 873 92 95",
                arrayOf(52.38976406377783, 16.995967919109045)
            ),

            Hospital(
                "Wojewódzki Szpital Zespolony",
                "dr Romana Ostrzyckiego",
                "Konin",
                "Szpitalna 45, (63) 240 44 02",
                arrayOf(52.19510049314684, 18.239177323846626)
            ),

            Hospital(
                "Wojewódzki Szpital Zespolony",
                "Ludwika Perzyny",
                "Kalisz",
                "Toruńska 7, (62) 757 91 84",
                arrayOf(51.76940611516646, 18.100693607083997)
            )
        ),

        "zachodniopomorskie" to listOf(

            Hospital(
                "107 Szpital Wojskowy z Przychodnią",
                "Samodzielny Publiczny Zakład Opieki Zdrowotnej",
                "Wałcz",
                "Kołobrzeska 44, 261 472 912",
                arrayOf(53.282024832949574, 16.459859334342013)
            ),

            Hospital(
                "Samodzielny Publiczny Wojewódzki Szpital Zespolony",
                "",
                "Szczecin",
                "Arkońska 4, (91) 813 94 43, (91) 813 94 54",
                arrayOf(53.4526724860138, 14.526418230280118)
            ),

            Hospital(
                "Szpital Wojewódzki",
                "Mikołaja Kopernika",
                "Koszalin",
                "Chałubińskiego 7, (94) 348 84 00",
                arrayOf(54.19809095261467, 16.213847654971598)
            )
        )
    )
}