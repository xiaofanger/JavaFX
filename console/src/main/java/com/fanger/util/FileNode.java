package com.fanger.util;

import java.util.List;

/**
 * 文件节点模型
 */
public class FileNode {

    public String id;
    public String text;
    public boolean leaf;
    public String iconCls;
    public String msg;
    public boolean expanded;
    public List<FileNode> children;
    public String type;
    public String filePath;
}
