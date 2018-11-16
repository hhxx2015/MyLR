package org.haohhxx.util;


import java.util.*;

/**
 * @author haozhenyuan
 * @date 2017/4/17
 * 工具类 对map进行各种排序
 *  jdk 1.8+
 */
public class MapUtil {

    /*
    static {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }
    */

    /**
     * 根据value中list的大小排序
     * @param map 要排序的map
     * @param asc 顺序，正序逆序
     * @param <K> key
     * @param <V> value
     * @return 排好的map
     *
     */
    public static <K, V extends Comparable<? super V>> Map<K,List<V>>
            sortByValueSize(Map<? extends K, ? extends List<V>> map,boolean asc){
        List<Map.Entry<? extends K,? extends List<V>>> list = new ArrayList<>(map.entrySet());
        if(asc){
            list.sort((o2, o1) -> (o1.getValue()).size()-(o2.getValue()).size());
        }else{
            list.sort(Comparator.comparing(o -> (o.getValue().size())));
        }
        LinkedHashMap<K,List<V>> sortMap = new LinkedHashMap<>();
        list.forEach(entry -> sortMap.put(entry.getKey(), entry.getValue()));
        return sortMap;
    }

    /**
     * 根据value内容排序
     * @param map 要排序的map
     * @param asc 正序逆序
     * @param <K> key
     * @param <V> value
     * @return 排好的map
     */
    public static <K, V extends Comparable<? super V>> Map<K,V>
            sortByValue (Map<? extends K, ? extends V> map,boolean asc){
        List<Map.Entry<? extends K,? extends V>> list = new ArrayList<>(map.entrySet());
        if(asc){
            list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        }else{
            list.sort(Comparator.comparing(o -> (o.getValue())));
        }
        LinkedHashMap<K,V> sortMap = new LinkedHashMap<>();
        list.forEach(entry -> sortMap.put(entry.getKey(), entry.getValue()));
        return sortMap;
    }

    public static <K, V extends Comparable<? super V>> Map<K,V>
            sortByValue (Map<? extends K, ? extends V> map){
        LinkedHashMap<K,V> sortMap = new LinkedHashMap<>();
        List<Map.Entry<? extends K,? extends V>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparing(o -> (o.getValue())));
        list.forEach(entry -> sortMap.put(entry.getKey(), entry.getValue()));
        return sortMap;
    }


    public static void main1(String[] args) {
        Map<Long,List<String>> map = new HashMap<>();
        ArrayList<String> list1 = new ArrayList<>();list1.add("1");
        ArrayList<String> list4 = new ArrayList<>();list4.add("1");list4.add("1");list4.add("2");
        ArrayList<String> list3 = new ArrayList<>();list3.add("1");list3.add("1");list3.add("2");list3.add("1");list3.add("2");
        ArrayList<String> list2 = new ArrayList<>();list2.add("1");list2.add("1");
        map.put(2L,list1);
        map.put(1L,list2);
        map.put(3L,list3);
        map.put(6L,list4);
        map = sortByValueSize(map,true);
        System.out.println(map.entrySet());
//        System.out.println(sortByValue(map,false));
    }

    public static void main(String[] args) {
        Map<Long,Integer> map = new HashMap<>(4);
        map .put(1L,1);
        map .put(2L,3);
        map .put(3L,2);
        System.out.println(sortByValue(sortByValue(map)));;
    }


}
