package com.example.swplanetapi.commom;

import com.example.swplanetapi.model.Planet;

public class PlanetConstants {

    public static final Planet PLANET = new Planet("name", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet("", "", "");

}
