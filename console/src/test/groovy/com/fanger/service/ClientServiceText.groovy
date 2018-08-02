package com.fanger.service

import java.util.regex.Matcher
import java.util.regex.Pattern

class ClientServiceText {

    static void main(String[] args) {
//        def msg = "java.util.List<java.lang.String, java.lang.Integer>"
        def msg = "java.util.List<java.util.Map<java.lang.String, java.lang.Object>>"
        println test2(msg)
    }

    static String getType(String comp) {
        if (comp.indexOf("<") > -1) {
            List<String> list = new ArrayList<String>()
            Pattern pattern = Pattern.compile("\\<(.*?)\\>")
            Matcher m = pattern.matcher(comp)
            int count = 1
            while (m.find()) {
                list.add(m.group(count))
                count++
            }
            String[] strs =  list.get(0).replaceAll("\\s", "").split(",")
            def lists = new ArrayList<String>()
            for (int i = 0; i < strs.size(); i++) {
                if (strs[i].lastIndexOf(".") > -1) {
                    strs[i] = strs[i].substring(strs[i].lastIndexOf(".") + 1)
                }
                lists << strs[i]
            }
            def type = comp.substring(0, comp.indexOf("<" - 1))
            if (type.lastIndexOf(".") > -1) {
                type = type.substring(type.lastIndexOf(".") +1)
            }
            def sb = new StringBuilder()
            sb.append(type).append("<").append(lists.join(", ")).append(">")
            comp = sb.toString()
        } else {
            if (comp.lastIndexOf(".") > -1) {
                comp = comp.substring(comp.lastIndexOf(".") + 1)
            }
        }
        return comp
    }

    static String test2(String comp) {
        return comp.replaceAll("\\b\\w+\\b|\\.", {
            String str ->
                if (!Character.isUpperCase(str.toCharArray()[0])) {
                    return ""
                } else {
                    return str
                }
        })
    }

}
