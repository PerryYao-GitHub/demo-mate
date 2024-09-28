package com.ypy.matebackend.utils;

import java.util.*;

public class AlgorithmUtils {

    /**
     * 编辑距离算法
     */
    private static int min(int x, int y, int z) {
        return Math.min(x, Math.min(y, z));
    }
    public static int minDist(List<String> tags1, List<String> tags2) {
        int len1 = tags1.size();
        int len2 = tags2.size();

        if (len1 * len2 == 0) return len1 + len2;
        Collections.sort(tags1);
        Collections.sort(tags2);
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i <= len2; i++) {
            dp[0][i] = i;
        }
        int cost;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (Objects.equals(tags1.get(i - 1), tags2.get(j - 1))) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                dp[i][j] = min(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost);
            }
        }
        return dp[len1][len2];
    }
}
