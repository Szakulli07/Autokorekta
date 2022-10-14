package com.example.imagepro;

import java.util.Arrays;
import java.util.List;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
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

    private static final List<Label> mainLabels = Arrays.asList(
            Label.CHASSIS,
            Label.ENGINE,
            Label.BODY,
            Label.ON_BOARD_COMPUTER
    );

    public static Label getLabelFromId(final int id){
        return values.get(id);
    }

    public static boolean isIdSmall(final int id){
        Label label = Label.getLabelFromId(id);
        return Label.isLabelSmall(label);
    }

    public static boolean isLabelSmall(final Label label){
        return Label.smallLabels.contains(label);
    }

    public static boolean isIdTeam(final int id){
        Label label = Label.getLabelFromId(id);
        return Label.isLabelTeam(label);
    }

    public static boolean isLabelTeam(final Label label){
        return Label.teamLabels.contains(label);
    }

    public static boolean isIdMain(final int id){
        Label label = Label.getLabelFromId(id);
        return Label.isLabelMain(label);
    }

    public static boolean isLabelMain(final Label label){
        return Label.mainLabels.contains(label);
    }

    public static Label labelGroup(final Label label){
        switch (label){
            case CHASSIS:
            case ENERGY_SAVING_SYSTEM:
                return CHASSIS;
            case ENGINE:
            case BATTERY:
                return ENGINE;
            case BODY:
            case SUNROOF:
                return BODY;
            case ON_BOARD_COMPUTER:
            case ELECTROMAGNETIC_ANTI_COLLISION_SYSTEM:
            case CONTROL_PANEL:
            case AUTOMATIC_STEERING:
                return ON_BOARD_COMPUTER;
            default:
                return BLANK;
        }
    }

    public static int getPossibleTypeCountFromId(final int id){
        Label label = Label.getLabelFromId(id);
        return Label.getPossibleTypeCountFromLabel(label);
    }

    public static int getPossibleTypeCountFromLabel(final Label label){
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
