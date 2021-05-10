package pk.usc.ai;

import pk.usc.ai.Unit.KBSentence;
import pk.usc.ai.Unit.Predicate;
import pk.usc.ai.ValueMap.PredicateMap;
import pk.usc.ai.diff.DiffKBStat;
import pk.usc.ai.diff.PredicateDiffStat;

import java.util.*;

public class FolEngine {
    public static ArrayList<KBSentence> kbQuery = new ArrayList<>();
    public static ArrayList<Boolean> QuerySol = new ArrayList<>();

    public static HashMap<Integer, KBSentence> mainKB = new HashMap<>();
    public static HashMap<Integer, Set<Integer>> mainPredicateNameMap = new HashMap<>();

    public static HashMap<Integer, KBSentence> tempKB;
    public static HashMap<Integer, Set<Integer>> tempPredicateNameMap;

    public static void addKBQuery(String query) {
        KBSentence kbSentence = new KBSentence(query);
        kbQuery.add(kbSentence);
    }

    public static void addKBSentenceIfPossible(String kbSentence) {
        KBSentence rule = new KBSentence(kbSentence);
        mainKB.put(rule.getIndex(), rule);
        addPredicateLocation(rule, mainPredicateNameMap);
    }

    public static boolean addKBSentenceIfPossible(
            KBSentence kbSentence,
            HashMap<Integer, Set<Integer>> predicateNameMap,
            HashMap<Integer, KBSentence> KB
    ) {
        for (KBSentence kb: KB.values()) {
            if(kb.isSimilar(kbSentence)){
                return false;
            }
        }
        if (kbSentence.isTrue()) {
            return false;
        }
        addPredicateLocation(kbSentence, predicateNameMap);
        KB.put(kbSentence.getIndex(), kbSentence);
        return true;
    }

    public static void addPredicateLocation(
            KBSentence kb,
            HashMap<Integer, Set<Integer>> predicateNameMap
    ) {
        for (Predicate p: kb._ORRule) {
            int factor = p._isNegative?(-1* p._predicateName): p._predicateName;
            if(!predicateNameMap.containsKey(factor)) predicateNameMap.put(factor, new HashSet<>());
            predicateNameMap.get(factor).add(kb._index);
        }
    }

    public static ArrayList<Integer> getPositiveIndex(
            int predicateName,
            HashMap<Integer, Set<Integer>> predicateNameMap,
            HashMap<Integer, KBSentence> KB
    ) {
        if (!predicateNameMap.containsKey(predicateName)) return new ArrayList<>();
        ArrayList<Integer> x = new ArrayList<>();
        for (Integer e : predicateNameMap.get(predicateName)) {
            if (KB.containsKey(e)) x.add(e);
        }
        return x;
    }

    public static ArrayList<Integer> getNegativeIndex(
            int predicateName,
            HashMap<Integer, Set<Integer>> predicateNameMap,
            HashMap<Integer, KBSentence> KB
    ) {
        if (!predicateNameMap.containsKey(-1*predicateName)) return new ArrayList<>();
        ArrayList<Integer> x = new ArrayList<>();
        for (Integer e : predicateNameMap.get(-1*predicateName)) {
            if (KB.containsKey(e)) x.add(e);
        }
        return x;
    }

    public static String matchKey(KBSentence k1, Predicate p1,
                                  KBSentence k2, Predicate p2) {
        return k1._index + "_" + p1._index + "#" + k2._index + "_" + p2._index;
    }

    private static boolean askKB(KBSentence query) {
        Set<String> set = new HashSet<>();
        while (true) {
            boolean workFound = false;
            for (Integer predicateName : PredicateMap._predicateMap.values()) {
                //System.out.println("PREDICATE:" + PredicateMap.findString(predicateName));

                for (Integer rule1: getPositiveIndex(predicateName, tempPredicateNameMap, tempKB)) {
                    for (Integer rule2: getNegativeIndex(predicateName, tempPredicateNameMap, tempKB)) {

                        if (!rule1.equals(rule2)) {
                            KBSentence kb1 = tempKB.get(rule1);
                            KBSentence kb2 = tempKB.get(rule2);

                            //if (kb1.isRule() && kb2.isRule())continue;
                            if (!(kb1._ORRule.size() == 1 || kb2._ORRule.size() == 1))continue;
                            for (Predicate p1 : kb1._ORRule) {
                                for (Predicate p2 : kb2._ORRule) {
                                    if (predicateName == p1._predicateName
                                            && Predicate.isOpposite(p1, p2)
                                            && !(set.contains(matchKey(kb1, p1, kb2, p2))
                                            && set.contains(matchKey(kb2, p2, kb1, p1)))) {
                                        PredicateDiffStat diffStat = Predicate.getDiff(p1, p2);

                                        if (diffStat != null) {

                                            DiffKBStat dks = new DiffKBStat(kb1, kb2, diffStat);
                                            KBSentence kbAfterUnification = dks.getKBAfterUnification(p1, p2);
                                            if (kbAfterUnification != null) {
                                                boolean b = addKBSentenceIfPossible(kbAfterUnification, tempPredicateNameMap, tempKB);

                                                if (b) {
                                                    if (kbAfterUnification._ORRule.isEmpty()){
                                                        return true;
                                                    }
                                                    /*
                                                    System.out.println(count);

                                                    System.out.println(count + "                    "+p1._kbSentence.toString()
                                                            + " ******* "
                                                            + p2._kbSentence.toString()
                                                            + " : " + p1.toString()
                                                            + " " + p2.toString());

                                                    //diffStat.printDiff();

                                                    System.out.println(count + ":  " +kbAfterUnification);*/
                                                    workFound = true;
                                                }
                                            }
                                        }

                                        set.add(matchKey(kb1, p1, kb2, p2));
                                        set.add(matchKey(kb2, p2, kb1, p1));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (workFound)continue;
            break;
        }
        return false;
    }

    public static void runEngine() {
        for (KBSentence query: kbQuery) {
            tempKB = new HashMap<>(mainKB);
            tempPredicateNameMap = new HashMap<>();
            mainPredicateNameMap.forEach((integer, integers) -> {
                tempPredicateNameMap.put(integer, new HashSet<>(integers));
            });

            query._ORRule.forEach(Predicate::reverseSign);
            tempKB.put(query._index, query);
            addPredicateLocation(query, tempPredicateNameMap);
            QuerySol.add(askKB(query));
        }
    }

    public static void printKB2() {
        mainKB.forEach((integer, sentence) -> {
            System.out.println(integer + ":");
            System.out.println(sentence.toString());
            System.out.println();
        });
    }
}
