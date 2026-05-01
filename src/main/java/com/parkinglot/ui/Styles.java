package com.parkinglot.ui;

/**
 * Centralized inline CSS strings for consistent styling across all scenes.
 * Colours: deep navy #1A2332, accent blue #2E86AB, light #F5F7FA, success green #27AE60, danger red #E74C3C
 */
public class Styles {

    // ── Root / Window ─────────────────────────────────────────────────────────
    public static final String ROOT_BG =
            "-fx-background-color: #1A2332;";

    public static final String CARD_BG =
            "-fx-background-color: #243447; -fx-background-radius: 12; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0, 0, 4);";

    // ── Typography ────────────────────────────────────────────────────────────
    public static final String TITLE =
            "-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; " +
                    "-fx-font-family: 'Courier New';";

    public static final String SUBTITLE =
            "-fx-font-size: 13px; -fx-text-fill: #7F8C9A; -fx-font-family: 'Courier New';";

    public static final String LABEL =
            "-fx-font-size: 13px; -fx-text-fill: #B0BEC5; -fx-font-family: 'Segoe UI';";

    public static final String SECTION_HEADER =
            "-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2E86AB; " +
                    "-fx-font-family: 'Segoe UI';";

    public static final String VALUE_TEXT =
            "-fx-font-size: 14px; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI';";

    // ── Input Fields ──────────────────────────────────────────────────────────
    public static final String TEXT_FIELD =
            "-fx-background-color: #1A2332; -fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #2E86AB; -fx-border-radius: 6; -fx-background-radius: 6; " +
                    "-fx-padding: 8 12; -fx-font-size: 13px; -fx-font-family: 'Segoe UI';";

    public static final String COMBO_BOX =
            "-fx-background-color: #1A2332; -fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #2E86AB; -fx-border-radius: 6; -fx-background-radius: 6; " +
                    "-fx-padding: 4 8; -fx-font-size: 13px;";

    // ── Buttons ───────────────────────────────────────────────────────────────
    public static final String BTN_PRIMARY =
            "-fx-background-color: #2E86AB; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; " +
                    "-fx-background-radius: 8; -fx-padding: 10 24; -fx-font-size: 13px; " +
                    "-fx-cursor: hand; -fx-font-family: 'Segoe UI';";

    public static final String BTN_PRIMARY_HOVER =
            "-fx-background-color: #1A6A8A; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; " +
                    "-fx-background-radius: 8; -fx-padding: 10 24; -fx-font-size: 13px; " +
                    "-fx-cursor: hand; -fx-font-family: 'Segoe UI';";

    public static final String BTN_SUCCESS =
            "-fx-background-color: #27AE60; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; " +
                    "-fx-background-radius: 8; -fx-padding: 10 24; -fx-font-size: 13px; " +
                    "-fx-cursor: hand; -fx-font-family: 'Segoe UI';";

    public static final String BTN_DANGER =
            "-fx-background-color: #E74C3C; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; " +
                    "-fx-background-radius: 8; -fx-padding: 10 24; -fx-font-size: 13px; " +
                    "-fx-cursor: hand; -fx-font-family: 'Segoe UI';";

    public static final String BTN_SECONDARY =
            "-fx-background-color: #3D5166; -fx-text-fill: #B0BEC5; -fx-font-weight: bold; " +
                    "-fx-background-radius: 8; -fx-padding: 10 24; -fx-font-size: 13px; " +
                    "-fx-cursor: hand; -fx-font-family: 'Segoe UI';";

    public static final String BTN_LOGOUT =
            "-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-weight: bold; " +
                    "-fx-border-color: #E74C3C; -fx-border-radius: 8; -fx-background-radius: 8; " +
                    "-fx-padding: 6 16; -fx-font-size: 12px; -fx-cursor: hand;";

    // ── Status Messages ───────────────────────────────────────────────────────
    public static final String MSG_SUCCESS =
            "-fx-text-fill: #27AE60; -fx-font-size: 13px; -fx-font-family: 'Segoe UI';";

    public static final String MSG_ERROR =
            "-fx-text-fill: #E74C3C; -fx-font-size: 13px; -fx-font-family: 'Segoe UI';";

    public static final String MSG_INFO =
            "-fx-text-fill: #2E86AB; -fx-font-size: 13px; -fx-font-family: 'Segoe UI';";

    // ── Sidebar / Nav ─────────────────────────────────────────────────────────
    public static final String SIDEBAR =
            "-fx-background-color: #111D2B; -fx-padding: 20;";

    public static final String NAV_BTN =
            "-fx-background-color: transparent; -fx-text-fill: #7F8C9A; " +
                    "-fx-font-size: 13px; -fx-cursor: hand; -fx-alignment: CENTER_LEFT; " +
                    "-fx-padding: 10 16; -fx-background-radius: 8; -fx-font-family: 'Segoe UI';";

    public static final String NAV_BTN_ACTIVE =
            "-fx-background-color: #2E86AB22; -fx-text-fill: #2E86AB; " +
                    "-fx-font-size: 13px; -fx-cursor: hand; -fx-alignment: CENTER_LEFT; " +
                    "-fx-padding: 10 16; -fx-background-radius: 8; -fx-font-weight: bold; " +
                    "-fx-font-family: 'Segoe UI';";

    // ── Display Board Spot Tiles ───────────────────────────────────────────────
    public static final String SPOT_FREE =
            "-fx-background-color: #1E4D2B; -fx-border-color: #27AE60; -fx-border-radius: 6; " +
                    "-fx-background-radius: 6; -fx-padding: 8; -fx-alignment: CENTER;";

    public static final String SPOT_OCCUPIED =
            "-fx-background-color: #4D1E1E; -fx-border-color: #E74C3C; -fx-border-radius: 6; " +
                    "-fx-background-radius: 6; -fx-padding: 8; -fx-alignment: CENTER;";

    // ── Table ─────────────────────────────────────────────────────────────────
    public static final String TABLE_VIEW =
            "-fx-background-color: #1A2332; -fx-border-color: #2E3D52; -fx-border-radius: 8;";

    public static final String DIVIDER =
            "-fx-border-color: #2E3D52; -fx-border-width: 0 0 1 0; -fx-padding: 0 0 12 0;";
}
