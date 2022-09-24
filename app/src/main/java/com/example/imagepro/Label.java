package com.example.imagepro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Label {
    A_TEAM,
    AUTOMATIC_STEERING,
    B_TEAM,
    BATTERY,
    BIOFUEL,
    BLANK,
    BODY,
    C_TEAM,
    CHASSIS,
    CONTROL_PANEL,
    D_TEAM,
    ELECTRIC,
    ELECTROMAGNETIC_ANTI_COLLISION_SYSTEM,
    ENERGY_SAVING_SYSTEM,
    ENGINE,
    HYBRID,
    ON_BOARD_COMPUTER,
    SOLAR,
    SUNROOF;

    private static final List<Label> values = Arrays.asList(Label.values());

    private static final List<Label> teamLabels = Arrays.asList(
            Label.A_TEAM,
            Label.B_TEAM,
            Label.C_TEAM,
            Label.D_TEAM
    );

    private static final List<Label> smallLabels = Arrays.asList(
            Label.BIOFUEL,
            Label.BLANK,
            Label.ELECTRIC,
            Label.HYBRID,
            Label.SOLAR
    );

    public static final Label getLabelFromId(final int id){
        return values.get(id);
    }

    public static final boolean isIdSmall(final int id){
        Label label = Label.getLabelFromId(id);
        return Label.isLabelSmall(label);
    }

    public static final boolean isLabelSmall(final Label label){
        return Label.smallLabels.contains(label);
    }

    public static final boolean isIdTeam(final int id){
        Label label = Label.getLabelFromId(id);
        return Label.isLabelTeam(label);
    }

    public static final boolean isLabelTeam(final Label label){
        return Label.teamLabels.contains(label);
    }

    public static final int getPossibleTypeCountFromId(final int id){
        Label label = Label.getLabelFromId(id);
        return Label.getPossibleTypeCountFromLabel(label);
    }

    public static final int getPossibleTypeCountFromLabel(final Label label){
        if(Label.isLabelTeam(label)){
            return -1;
        }else if(Label.isLabelSmall(label)){
            return 0;
        }else if(label == Label.CHASSIS || label == Label.BODY){
            return 2;
        }else if(label == Label.ENGINE || label == Label.ON_BOARD_COMPUTER){
            return 3;
        }else {
            return 1;
        }
    }
}
