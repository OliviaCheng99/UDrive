package com.capstone.udrive.entity.enums;


import org.apache.commons.lang3.ArrayUtils;

public enum FileTypeEnums {
    VIDEO(FileCategoryEnums.VIDEO, 1, new String[]{".mp4", ".avi", ".rmvb", ".mkv", ".mov"}, "video"),
    MUSIC(FileCategoryEnums.MUSIC, 2, new String[]{".mp3", ".wav", ".wma", ".mp2", ".flac", ".midi", ".ra", ".ape", ".aac", ".cda"}, "audio"),
    IMAGE(FileCategoryEnums.IMAGE, 3, new String[]{".jpeg", ".jpg", ".png", ".gif", ".bmp", ".dds", ".psd", ".pdt", ".webp", ".xmp", ".svg", ".tiff"}, "image"),
    PDF(FileCategoryEnums.DOC, 4, new String[]{".pdf"}, "pdf"),
    WORD(FileCategoryEnums.DOC, 5, new String[]{".docx"}, "word"),
    EXCEL(FileCategoryEnums.DOC, 6, new String[]{".xlsx"}, "excel"),
    TXT(FileCategoryEnums.DOC, 7, new String[]{".txt"}, "text"),
    PROGRAME(FileCategoryEnums.OTHERS, 8, new String[]{".h", ".c", ".hpp", ".hxx", ".cpp", ".cc", ".c++", ".cxx", ".m", ".o", ".s", ".dll", ".cs",
            ".java", ".class", ".js", ".ts", ".css", ".scss", ".vue", ".jsx", ".sql", ".md", ".json", ".html", ".xml"}, "code"),
    ZIP(FileCategoryEnums.OTHERS, 9, new String[]{"rar", ".zip", ".7z", ".cab", ".arj", ".lzh", ".tar", ".gz", ".ace", ".uue", ".bz", ".jar", ".iso",
            ".mpq"}, "compressed"),
    OTHERS(FileCategoryEnums.OTHERS, 10, new String[]{}, "other");

    private final FileCategoryEnums category;
    private final Integer type;
    private final String[] suffixs;
    private final String desc;

    FileTypeEnums(FileCategoryEnums category, Integer type, String[] suffixs, String desc) {
        this.category = category;
        this.type = type;
        this.suffixs = suffixs;
        this.desc = desc;
    }

    public static FileTypeEnums getFileTypeBySuffix(String suffix) {
        for (FileTypeEnums item : FileTypeEnums.values()) {
            if (ArrayUtils.contains(item.getSuffixs(), suffix)) {
                return item;
            }
        }
        return FileTypeEnums.OTHERS;
    }

    public static FileTypeEnums getByType(Integer type) {
        for (FileTypeEnums item : FileTypeEnums.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public String[] getSuffixs() {
        return suffixs;
    }

    public FileCategoryEnums getCategory() {
        return category;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
