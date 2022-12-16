package com.bura.common.gameobject

import com.bura.common.engine.Engine

abstract class StaticEntity(engine: Engine, centerX: Float, centerY: Float) :
    GameObject(engine, centerX, centerY) {
}