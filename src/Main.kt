import java.util.*
import kotlin.math.pow


private val chromosomeList = mutableListOf<Int>()
    private val chromosomeList2 = mutableListOf<Int>()
    private val valueList = mutableListOf<Int>()
    private val chromosomeListBinar = mutableListOf<String>()
    private val chromosomeListBinar2 = mutableListOf<String>()
    private val percentageValueList = mutableListOf<Double>()
    private val percentageRangeList = mutableListOf<PercentageRangeClass>()

fun main(){
    val scanner = Scanner(System.`in`)
    print("Podaj liczbę chromosomów: ")
    val chromosomesNumber: Int = scanner.nextInt()
    println("\nPodaj współczynniki a,b,c,d funkcji ax^3 + bx^2 + cx + d w kolejności: a ,b ,c ,d: ")
    val a: Int = scanner.nextInt()
    val b: Int = scanner.nextInt()
    val c: Int = scanner.nextInt()
    val d: Int = scanner.nextInt()
    println("\nPodaj Pk i Pm jako wartość od 0 do 1 w typie double: ")
    val pk: Double = scanner.nextDouble()
    val pm: Double = scanner.nextDouble()
    print("\nPodaj liczbę maksymalnych wystąpień wartości funkcji przystosowania: ")
    val numOfMaxFitFunOccur: Int = scanner.nextInt()
    var numOfFitListIterations = 0
    var numOfIterations = 0

    for(i in 0 until chromosomesNumber){
        val randNumHelper: Int = randomNumberChromosome()
        chromosomeList.add(i, randNumHelper)
        chromosomeList2.add(i, randNumHelper)
        valueList.add(i, calcFun(a,b,c,d,chromosomeList[i]))
    }
    println("Początkowa pula chromosomów: $chromosomeList")

    for (i in 0 until chromosomesNumber){
        var binarValue: String = Integer.toBinaryString(chromosomeList[i])

        when (binarValue.length) {
            4 -> {
                binarValue = "0$binarValue"
            }
            3 -> {
                binarValue = "00$binarValue"
            }
            2 -> {
                binarValue = "000$binarValue"
            }
            1 -> {
                binarValue = "0000$binarValue"
            }
        }
        chromosomeListBinar.add(i, binarValue)
        chromosomeListBinar2.add(i, binarValue)
    }
    for(i in 0 until chromosomesNumber){
        percentageValueList.add(0.0)
        percentageRangeList.add(i, PercentageRangeClass(0.0, 0.0))
    }
    var final = 0

    while (true) {
        var percentageIterationValue = 0.0
        var aValuePercent: Double
        var valueOfFitFun = 0

        for (i in 0 until chromosomesNumber){
            val currValue  = valueList[i]
            valueOfFitFun += currValue
        }

        if (valueOfFitFun > final){
            final = valueOfFitFun
            numOfFitListIterations = 1
        }
        else if (valueOfFitFun == final){
            numOfFitListIterations++
        }

        if(numOfFitListIterations == numOfMaxFitFunOccur){
            break
        }

        for (i in 0 until chromosomesNumber) {
            percentageValueList[i] =
                percentageValue(valueList[i], valueOfFitFun)
        }

        for(i in 0 until chromosomesNumber){
            aValuePercent = percentageIterationValue
            percentageIterationValue += percentageValueList[i]
            percentageRangeList[i] = PercentageRangeClass(aValuePercent, percentageIterationValue)
        }

        for( i in 0 until chromosomesNumber){
            val x: Double = randomPercentageNumber()

            for( j in 0 until chromosomesNumber){
                if (x> percentageRangeList[j].a && x<percentageRangeList[j].b)
                    chromosomeList[i] = chromosomeList2[j]
            }
        }

        for (i in 0 until chromosomesNumber){
            var binarValue = Integer.toBinaryString(chromosomeList[i])
            
            when (binarValue.length) {
                4 -> {
                    binarValue = "0$binarValue"
                }
                3 -> {
                    binarValue = "00$binarValue"
                }
                2 -> {
                    binarValue = "000$binarValue"
                }
                1 -> {
                    binarValue = "0000$binarValue"
                }
            }
            chromosomeListBinar[i] = binarValue
            chromosomeListBinar2[i] = binarValue
        }
        var pkHelperNumber = 0

        for(i in 0 until chromosomesNumber/2){
            val x: Double = randomPercentageNumber()
            val random16: Int = random16()
            val random162: Int = random16()
            pkHelperNumber += 2
            val lokus: Int = randomNumberLokusPk()

            if(x < pk){
                val begOneOf2: String = chromosomeListBinar2[random16-1].substring(0, lokus)
                val begTwoOf2: String = chromosomeListBinar2[random162-1].substring(0, lokus)
                val endOneOf2: String = chromosomeListBinar2[random16-1].substring(lokus, chromosomeListBinar[i].length)
                val endTwoOf2: String = chromosomeListBinar2[random162-1].substring(lokus, chromosomeListBinar[i].length)

                val one: String = begOneOf2+endTwoOf2
                val two: String = begTwoOf2+endOneOf2

                chromosomeListBinar[pkHelperNumber-2] = one
                chromosomeListBinar[pkHelperNumber-1] = two
            }
        }

        for(i in 0 until chromosomesNumber){
            val x: Double = randomPercentageNumber()
            val lokus: Int = randomNumberLokusPm()

            if(x < pm){
                val beg: String = chromosomeListBinar[i].substring(0, lokus-1)
                val end: String = chromosomeListBinar[i].substring(lokus)
                val mutate: String = chromosomeListBinar[i].substring(lokus-1, lokus)

                if(mutate == "0"){
                    val chromosomeBinarFinal = beg+"1"+end
                    chromosomeListBinar[i] = chromosomeBinarFinal
                } else if(mutate == "1"){
                    val chromosomeBinarFinal = beg+"0"+end
                    chromosomeListBinar[i] = chromosomeBinarFinal
                }
            }
        }

        for(i in 0 until chromosomesNumber){
            val decimal: Int  = chromosomeListBinar[i].toInt(2)
            chromosomeList[i] = decimal
        }

        for(i in 0 until chromosomesNumber){
            val calculatedValue = calcFun(a,b,c,d, chromosomeList[i])
            valueList[i] = calculatedValue
            chromosomeList2[i] = chromosomeList[i]
        }
        numOfIterations++

    }
    println(" | Liczba iteracji: $numOfIterations | ")
    println(" | Suma funkcji przystosowania: $final | ")

    for(i in 0 until chromosomesNumber){
        val chNumber:Int = i+1
        print(" | CH$chNumber =  ${chromosomeListBinar[i]} | ")
        print("Fenotyp = ${chromosomeList[i]} | ")
        println("Funkcja przystosowania = ${valueList[i]} | ")
    }
}
fun randomNumberChromosome(): Int {
    return (1..31).random()
}

fun randomNumberLokusPm(): Int {
    return (1..5).random()
}

fun randomNumberLokusPk(): Int {
 return (1..4).random()
}

fun random16(): Int {
    return (1..6).random()
}

fun randomPercentageNumber(): Double {
    return (Math.random() * (1 - 0))
}

fun calcFun(a: Int, b: Int, c: Int, d: Int, x: Int): Int {
    return (((a * x.toDouble().pow(3) + b * (x.toDouble().pow(2)) + c * x + d).toInt()))
}

fun percentageValue(a: Int, b: Int): Double {
    val c = a.toDouble()
    val d = b.toDouble()
    return c / d
}

class PercentageRangeClass(var a: Double, var b: Double)