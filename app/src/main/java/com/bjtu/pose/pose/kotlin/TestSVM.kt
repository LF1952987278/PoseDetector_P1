package com.bjtu.pose.pose.kotlin

class TestSVM {
    companion object {
//        fun testModel() {
//            val svm = Object2File.readObjectFromFile(svmReadPath) as SVM<DoubleArray>
//            println("phone pose = ${Constant.phonePose}, seq len = ${Constant.seqLen}, stride = ${Constant.stride}, reject sec = ${Constant.rejectSec}")
//
//            val posePathMap: Map<String, MutableList<String>> =
//                mutableMapOf(FLAT to mutableListOf(Constant.flatPath),
//                    CALLING to mutableListOf(Constant.callingPath),
//                    POCKET to mutableListOf(Constant.pocketPath),
//                    BAG to mutableListOf(Constant.bagPath))
//
//            val sampleTestRaw: MutableList<MutableList<RawElem>> = mutableListOf() // m * n * (5*3*3)
//            val sampleTestFtr: MutableList<MutableList<FtrElem>> = mutableListOf() // m * n * (5*3*3)
//            val fileNameLs = mutableListOf<String>()
//
//            for ((_pose, _pathList) in posePathMap) { // 对于每一个姿态所对应的数据路径们，这里一个姿态对应一个路径，可以对应多个路径
//                if (Constant.phonePose != ALL && _pose != Constant.phonePose)
//                    continue
//                for (_path in _pathList) { // 遍历路径列表，由于一个姿态对一个路径，这里只循环一次
//                    val filePathMap = listDataSetFiles(dft_path = _path, POSE = _pose) // 得到这个路径下所有文件
//                    for ((datasetType, filePathList) in filePathMap) { // 循环两次，分别是"train"和"test"
//                        // print
//                        for(filePath in filePathList) { // 对于每一个csv文件
//                            println("read $filePath")
//                            val tsSenMap = parseSenFromFile(filePath) // 将csv文件转为tsSenMap
//                            val rawFtrItem: RawFtrLists = jointSenUnit2FtrStr(tsSenMap = tsSenMap, SEQ_LEN = Constant.seqLen, STRIDE = Constant.stride,
//                                REJECT_SEC = Constant.rejectSec, POSE = _pose)
//                            val raw: MutableList<RawElem> = rawFtrItem.srl
//                            val ftr: MutableList<FtrElem> = rawFtrItem.sfl  //   n * (5*3*3)
//
//                            if (ftr.size != 0) {
//                                when (datasetType) {
//                                    TRAIN -> {
//
//                                    }
//                                    TEST -> { // 对于测试集， 会把测试集文件名记录 由代码逻辑可知 一个文件对应sampleTestFtr列表中一个元素
//                                        sampleTestRaw.add(raw) // m * n * (5*3*3)
//                                        sampleTestFtr.add(ftr) // m * n * (5*3*3)
//                                        fileNameLs.add(filePath.trim())
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            //Test
//            val testDataList: MutableList<MutableList<List<List<List<Double>>>>> = mutableListOf()
//            val testGroundTruthList: MutableList<MutableList<Int>> = mutableListOf()
//            // sampleTestFtr: m*n*45
//            // item: n*45
//            for (item in sampleTestFtr) {
//                val tmpData: MutableList<List<List<List<Double>>>> = mutableListOf()
//                val tmpTruth: MutableList<Int> = mutableListOf()
//                // smallItem: 45
//                for (smallItem in item) {
//                    tmpData.add(smallItem.getContent())
//                    tmpTruth.add(smallItem.poseID)
//                }
//                testDataList.add(tmpData)
//                testGroundTruthList.add(tmpTruth)
//            }
//            println("Test Data Process Finished!")
//
//            // Test Process
//            val allTestSrLabel = mutableListOf<Int>()
//            val allTestReLabel = mutableListOf<Int>()
//            val allTestTruth = mutableListOf<Int>()
//            for ((index, data) in testDataList.withIndex()) {
//                val dlc: MutableList<DoubleArray> = mutableListOf()
//                for (item3 in data) {
//                    val dlcItem: MutableList<Double> = mutableListOf()
//                    for (item2 in item3) {
//                        for (item1 in item2) {
//                            for (item0 in item1) {
//                                dlcItem.add(item0)
//                            }
//                        }
//                    }
//                    when (dlcItem.size) {
//                        45 -> {
//                            dlc.add(dlcItem.toDoubleArray())
//                        }
//                        else -> {
//                            throw Error("Very Diff Error Occor! The 45 size is ${dlcItem.size}")
//                        }
//                    }
//                }
//
//                val testData: Array<DoubleArray> = dlc.toTypedArray()
//                val testTruth: IntArray = testGroundTruthList[index].toIntArray()
//
//                // 生成 testLabel testLabel 是我们通过svm来预测的
//                // 对于testData中的每一个DoubleArray都生成一个Int值最后形成一个列表
//                val testLabelList: MutableList<Int> = mutableListOf()
//                for (item in testData) { // item: DoubleArray
//                    testLabelList.add(svm.predict(item))
//                }
//
//                // println("Size: Size: ${testLabelList.size}")
//
//                val testLabel: IntArray = testLabelList.toIntArray()
//                // 修正预测到的testLabel以达到更高的预测精度， 得到reTesLabel
//                // 将testLabel 的第一个元素作为 列表的第一个元素
//                val reTesLabel = getReLabels(testLabel)
//
////        println("Size of test data is ${testData.size}")
////        println("Size of reTesLabel is ${reTesLabel.size}")
//
//                println("File: ${fileNameLs[index]}")
////         对testTruth(实际类标签) 和 testlabel（预测类标签）
//                println("Test Set accuracy score: ${accuracy(testTruth, testLabel)}")
//                // 对testTruth（实际类标签） 和 reTesLabel （修改过的预测类标签）
//                println("Re Test Set accuracy score: ${accuracy(testTruth, reTesLabel.toIntArray())}")
//
//                allTestSrLabel.addAll(testLabel.toList())
//                allTestReLabel.addAll(reTesLabel)
//                allTestTruth.addAll(testTruth.toList())
//            }
//
//            println("All Test Sets Accuracy Score: ${accuracy(allTestTruth.toIntArray(), allTestSrLabel.toIntArray())}")
//            println("All Re Test Sets Accuracy score: ${accuracy(allTestTruth.toIntArray(),allTestReLabel.toIntArray())}")
//
//        }
    }
}


