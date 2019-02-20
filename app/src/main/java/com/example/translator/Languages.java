package com.example.translator;

public class Languages {
    private static String[] langs = {"Afrikaans", "Albanian", "Amharic", "Arabic", "Armenian", "Azerbaijan", "Bashkir", "Basque", "Belarussian", "Bengali", "Bosnian", "Bulgarian", "Catalan", "Cebuano", "Chinese", "Croatian", "Czech", "Danish", "Dutch", "English", "Esperanto", "Estonian", "Finnish", "French", "Galician", "Georgian", "German", "Greek", "Gujarati", "Haitian", "Hebrew", "Hindi", "Hungarian", "Icelandic", "Indonesian", "Irish", "Italian", "Japanese", "Javanese", "Kannada", "Kazakh", "Korean", "Kyrgyz", "Latin", "Latvian", "Lithuanian", "Luxembourg", "Macedonian", "Malagasy", "Malay", "Malayalam", "Maltese", "Maori", "Marathi", "Mari", "Mari", "Mongolian", "Nepali", "Norwegian", "Papiamento", "Persian", "Polish", "Portuguese", "Punjabi", "Romanian", "Russian", "Scottish", "Serbian", "Sinhala", "Slovak", "Slovenian", "Spanish", "Spit", "Sundanese", "Swahili", "Swedish", "Tagalog", "Tajik", "Tamil", "Tatar", "Telugu", "Thai", "Turkish", "Udmurt", "Ukrainian", "Urdu", "Uzbek", "Vietnamese", "Welsh", "Yiddish"};
    private static String[] langCode = {"af", "sq", "am", "ar", "hy", "az", "ba", "eu", "be", "bn", "bs", "bg", "ca", "ceb", "zh", "hr", "cs", "da", "nl", "en", "eo", "et", "fi", "fr", "gl", "ka", "de", "el", "gu", "ht", "he", "hi", "hu", "is", "id", "ga", "it", "ja", "jv", "kn", "kk", "ko", "ky", "la", "lv", "lt", "lb", "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mhr", "mrj", "mn", "ne", "no", "pap", "fa", "pl", "pt", "pa", "ro", "ru", "gd", "sr", "si", "sk", "sl", "es", "xh", "su", "sw", "sv", "tl", "tg", "ta", "tt", "te", "th", "tr", "udm", "uk", "ur", "uz", "vi", "cy", "yi"};


    private static String bufferLang1 = "";
    private static String bufferLang2 = "";
    private static String buferForAutoDetectedLang = "";



    public static String getBuferForAutoDetectedLang() {
        return buferForAutoDetectedLang;
    }

    public static void setBuferForAutoDetectedLang(String buferForAutoDetectedLang) {
        Languages.buferForAutoDetectedLang = buferForAutoDetectedLang;
    }

    public static String getBufferLang1() {
        return bufferLang1;
    }

    public static int getItemPosition(String lang){
        int position = 0;
        for (int i = 0; i <= langCode.length && (position == 0); i++) {
            if (langCode[i].equals(lang)){
                position = i;
                break;
            }
        }

        return position;
    }

    public static void setBufferLang1(String bufferLang1) {
        Languages.bufferLang1 = bufferLang1;
    }

    public static String getBufferLang2() {
        return bufferLang2;
    }

    public static void setBufferLang2(String bufferLang2) {
        Languages.bufferLang2 = bufferLang2;
    }

    public static String[] getLangs(){
        return langs;
    }
    public static String getLangCode(int i){
        return langCode[i];
    }
}
