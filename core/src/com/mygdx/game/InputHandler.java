package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;

import java.util.HashSet;
import java.util.Set;

public class InputHandler implements InputProcessor {

    private Camera camera;

    private Set<Integer> pressedKeys;

    public InputHandler(Camera camera) {
        pressedKeys = new HashSet<Integer>();
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }

        pressedKeys.add(keycode);

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        pressedKeys.remove(keycode);

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void update() {
        if (pressedKeys.contains(Input.Keys.W)) {
            camera.translate(1f, 0f, 0f);
        }

        if (pressedKeys.contains(Input.Keys.S)) camera.translate(-1f, 0f, 0f);
        if (pressedKeys.contains(Input.Keys.A)) camera.translate(0f, 0f, 1f);
        if (pressedKeys.contains(Input.Keys.D)) camera.translate(0f, 0f, -1f);
        if (pressedKeys.contains(Input.Keys.X)) camera.translate(0f, 1f, 0f);
        if (pressedKeys.contains(Input.Keys.C)) camera.translate(0f, -1f, 0f);

        camera.update();
    }
}
