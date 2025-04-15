package io.homo.grapheneui.core.icon;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.homo.grapheneui.GrapheneUIMod;
import io.homo.grapheneui.nanovg.NanoVGFont;
import io.homo.grapheneui.utils.FileReadHelper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FluentIconPack {
    public static final Map<String, Map<String, Icon>> ICON = new HashMap<>();
    public static final NanoVGFont ICON_FONT;
    private static final String ICON_FONT_FILE = "/fonts/FluentUIIcons.ttf";
    private static final String ICON_MAP_FILE = "/fonts/FluentUIIconMap.json";
    public static Map<String, String> ICON_MAP;

    static {
        ICON.put("filled", new HashMap<>());
        ICON.put("light", new HashMap<>());
        ICON.put("regular", new HashMap<>());
        ICON_FONT = new NanoVGFont("FluentUIIcons", ICON_FONT_FILE);
    }

    public static Map<String, Icon> filled() {
        return ICON.get("filled");
    }

    public static Map<String, Icon> light() {
        return ICON.get("light");
    }

    public static Map<String, Icon> regular() {
        return ICON.get("regular");
    }

    public static void load() {
        ICON_FONT.load();
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        ICON_MAP = gson.fromJson(String.join("\n", FileReadHelper.readText(ICON_MAP_FILE)), type);
        for (String name : ICON_MAP.keySet()) {
            String code = ICON_MAP.get(name);
            char[] chars = Character.toChars(Integer.parseInt(code.substring(2), 16));
            code = new String(chars);
            String style = name.split("_")[name.split("_").length - 1];
            ICON.get(style).put(
                    name,
                    new Icon(
                            style,
                            name,
                            code
                    )
            );
        }
        GrapheneUIMod.LOGGER.info("FluentUI图标加载完成");
    }
}
