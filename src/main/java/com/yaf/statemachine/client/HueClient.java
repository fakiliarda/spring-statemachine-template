package com.yaf.statemachine.client;

public interface HueClient {

    void flickLights();

    //LightId: 1(Hue LIGHTSTRIP), 2(Hue GO), 3(Hue LAMP)
    void changeLightState(int lightId, boolean on);

    void changeAllLightStates(boolean on);

    //LightId: 1(Hue LIGHTSTRIP), 2(Hue GO), 3(Hue LAMP)
    //Color: 0-65535
    void changeLightStateOnAndColor(int lightId, int color);

    //Color: 0-65535
    void changeAllLightStatesOnAndColor(int color);

    //Color: 0-65535
    //Brightness: 0-255
    void changeAllLightStatesOnAndColorAndDim(int color, int brightness);

}
