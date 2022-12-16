package com.bura.common.gameobject

import com.bura.common.engine.Engine

abstract class Entity(engine: Engine, centerX: Float, centerY: Float) :
    GameObject(engine, centerX, centerY) {
}