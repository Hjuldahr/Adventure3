package com.example.adventure.creature;

import com.example.adventure.creature.Alignment.SubAlignments;

public abstract class Alignment {
    public enum SubAlignments {
        NEUTRAL,
        LAWFUL,
        CHAOTIC,
        GOOD,
        EVIL,
        NONE
    }

    public enum Alignments {
        LAWFUL_GOOD("Lawful Good", SubAlignments.LAWFUL, SubAlignments.GOOD),
        NEUTRAL_GOOD("Neutral Good", SubAlignments.NEUTRAL, SubAlignments.GOOD),
        CHAOTIC_GOOD("Chaotic Good", SubAlignments.CHAOTIC, SubAlignments.GOOD), 
        
        LAWFUL_NEUTRAL("Lawful Neutral", SubAlignments.LAWFUL, SubAlignments.NEUTRAL),
        TRUE_NEUTRAL("True Neutral", SubAlignments.NEUTRAL, SubAlignments.NEUTRAL),
        CHAOTIC_NEUTRAL("Chaotic Neutral", SubAlignments.CHAOTIC, SubAlignments.NEUTRAL), 
        
        LAWFUL_EVIL("Lawful Evil", SubAlignments.LAWFUL, SubAlignments.EVIL),
        NEUTRAL_EVIL("Neutral Evil", SubAlignments.NEUTRAL, SubAlignments.EVIL),
        CHAOTIC_EVIL("Chaotic Evil", SubAlignments.CHAOTIC, SubAlignments.EVIL),

        UNALIGNED("Unaligned", SubAlignments.NONE, SubAlignments.NONE);

        private final String name;
        private final SubAlignments ethic;
        private final SubAlignments moral;

        Alignments(String name, SubAlignments ethic, SubAlignments moral) {
            this.name = name;
            this.ethic = ethic;
            this.moral = moral;
        }

        public String getName() { this.name; }
        public SubAlignments getEthic() { this.ethic; }
        public SubAlignments getMoral() { this.moral; }

        public boolean hasSubAlignment(SubAlignments subAlignment) {
            return this.ethic.equals(subAlignment) || this.moral.equals(subAlignment);
        }
    }
}