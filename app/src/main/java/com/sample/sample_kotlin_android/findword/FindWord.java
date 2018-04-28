package com.sample.sample_kotlin_android.findword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FindWord {

    public static String[][] boardArr = { { "A", "K",  "S", "T" },
            { "P", "ES", "O", "E" },
            { "A", "T",  "P", "T" },
            { "S", "T",  "A", "E" } };
    static List<String> wordList = new ArrayList<String>();

    /**
     * 查找单词是否在表格中，可以从八个方向任意组合
     * @param word
     * @return
     */
    public static boolean findWord(String word) {

        char [] wdChArr = word.toCharArray();   // 单词字母数组

        List<List<int[]>> wordCoordinateList = new ArrayList<List<int[]>>();  // 单词的每个字母所在的坐标，一个字母可能多次出现

        for(int k = 0; k < wdChArr.length; k++) {
            char ch = wdChArr[k];

            ArrayList<int[]> charCoordinateList = new ArrayList<int[]>();       //单词中某个字母所在的坐标

            for(int i = 0; i < 4; i++) {
                for(int j = 0; j < 4; j++) {
                    String chStr = boardArr[i][j];
                    if(chStr.length() > 1) {     // 一个格子中字母组合的情况
                        String tmp = "";
                        for(int n = 0; n < chStr.length(); n++) {   // 字母组合从左到右看是否匹配
                            if(k + n < wdChArr.length) {
                                tmp += wdChArr[k + n];
                            }
                        }
                        if(tmp.equals(chStr)) {
                            k = k + chStr.length() - 1;
                            int []coordinate = new int[] {i, j};
                            charCoordinateList.add(coordinate);
                            j = 4; i = 4;          // 匹配到字母组合，跳出
                        }
                    } else {
                        if(chStr.equals(String.valueOf(ch))) {
                            int []coordinate = new int[] {i, j};
                            charCoordinateList.add(coordinate);
                        }
                    }
                }
            }

            if(charCoordinateList.size() < 1) {
                return false;
            } else {
                wordCoordinateList.add(charCoordinateList);
            }

        }

        if(getRouteList(wordCoordinateList).size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 根据字母所在位置，遍历所有的字母路径，如果存在相邻并不交叉的路径，表示匹配单词
     * 递归方式
     * @param wordCoordinateList
     * @return
     */
    static private List<List<int[]>>getRouteList(List<List<int[]>> wordCoordinateList) {

        if(wordCoordinateList.size() == 1) {
            List<List<int[]>> resultList = new ArrayList<List<int[]>>();
            for(int []wordCoordiate : wordCoordinateList.get(0)) {
                List<int []> subRoute = new ArrayList<int []>();
                subRoute.add(wordCoordiate);
                resultList.add(subRoute);
            }
            return resultList;

        } else {

            List<List<int[]>> resultList = new ArrayList<List<int[]>>();

            List<int[]> firstWordCoordinateList = wordCoordinateList.get(0);
            List<List<int[]>> subList = new ArrayList<List<int[]>>();
            for(int i = 1; i < wordCoordinateList.size(); i++) {
                subList.add(wordCoordinateList.get(i));
            }

            //递归的方式,获取相邻且不交叉的路径
            List<List<int[]>> subRouteList = getRouteList(subList);

            for(int[] firstWordCoordinate : firstWordCoordinateList) {

                // 遍历所有已存在路径, 和前一个单词做匹配,看是否相邻且不交叉
                for(List<int []> subRoute : subRouteList) {

                    int rd = Math.abs(subRoute.get(0)[0] - firstWordCoordinate[0]);
                    int cd = Math.abs(subRoute.get(0)[1] - firstWordCoordinate[1]);
                    // 判断是否相邻(8个方向)
                    if((rd == 1 && cd == 0) || (rd == 1 && cd == 1) || (rd == 0 && cd == 1)) {
                        // 判断是否有交叉
                        boolean isExists = false;
                        for(int []routeItem : subRoute) {
                            if(routeItem[0] == firstWordCoordinate[0] && routeItem[1] == firstWordCoordinate[1]) {
                                isExists = true;
                                break;
                            }
                        }

                        if(!isExists) {
                            List<int []> route = new ArrayList<int []>();
                            route.add(firstWordCoordinate);
                            route.addAll(subRoute);
                            resultList.add(route);
                        }

                    }
                }

            }

            return resultList;
        }

    }

    static void readDictFile() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File("res/dict.txt")));
        String line = null;

        while((line = br.readLine()) != null) {
            if(line.length() > 0 && line.charAt(0) >= 'a' && line.charAt(0) <= 'z') {
                wordList.add(line.toUpperCase());
            }
        }
    }

}
