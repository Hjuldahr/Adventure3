package com.example.adventure.creature;

public enum Langauges {
    AARAKOCRA("Aarakocra"),
    ABYSSAL("Abyssal"),
    AQUAN("Aquan"),
    AURAN("Auran"),
    BLINK_DOG("Blink Dog"),
    BULLYWUG("Bullywug"),
    CELESTIAL("Celestial"),
    COMMON("Common"),
    DEEP_SPEECH("Deep Speech"),
    DRACONIC("Draconic"),
    DRUIDIC("Druidic"),
    DWARVISH("Dwarvish"),
    ELVISH("Elvish"),
    GIANT("Giant"),
    GIANT_EAGLE("Giant Eagle"),
    GIANT_ELK("Giant Elk"),
    GIANT_OWL("Giant Owl"),
    GITH("Gith"),
    GNOLL("Gnoll"),
    GNOMISH("Gnomish"),
    GOBLIN("Goblin"),
    GRELL("Grell"),
    HALFLING("Halfling"),
    HOOK_HORROR("Hook Horror"),
    IGNAN("Ignan"),
    INFERNAL("Infernal"),
    KRAUL("Kraul"),
    LOXODON("Loxodon"),
    MERFOLK("Merfolk"),
    MINOTAUR("Minotaur"),
    MODRON("Modron"),
    ORCISH("Orcish"),
    OTYUGH("Otyugh"),
    PRIMORDIAL("Primordial"),
    SAHUAGIN("Sahuagin"),
    SLAAD("Slaad"),
    SPHINX("Sphinx"),
    SYLVAN("Sylvan"),
    TERRAN("Terran"),
    THIEVES_CANT("Thieves Cant"),
    THRI_KREEN("Thri-kreen"),
    TROGLODYTE("Troglodyte"),
    UMBER_HULK("Umber Hulk"),
    UNDERCOMMON("Undercommon"),
    VEDALKEN("Vedalken"),
    WINTER_WOLF("Winter Wolf"),
    WORG("Worg"),
    YETI("Yeti");
    
    private final String name;

    Langauges(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
