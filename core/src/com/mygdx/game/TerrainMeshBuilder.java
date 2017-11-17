package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TerrainMeshBuilder {

    public static final int MESH_SIZE = 32;

    private int[][] heightMap;

    private int terrainSize;

    public TerrainMeshBuilder(int terrainSize) {
        this.terrainSize = terrainSize;
        heightMap = new int[4096][4096];
//        heightMap = new int[terrainSize * MESH_SIZE + 2][terrainSize * MESH_SIZE + 2];
//        createHeightMap();
        buildTerrainFromHeightmap();
    }

    public Model buildTerrainMesh() {
        MeshBuilder meshBuilder = new MeshBuilder();

        Vector3 x = new Vector3();
        Vector3 y = new Vector3();
        Vector3 z = new Vector3();

        List<Mesh> meshList = new ArrayList<Mesh>();

        for (int width = 0; width < terrainSize; width++) {
            for (int height = 0; height < terrainSize; height++) {
                meshBuilder.begin(Usage.Normal | Usage.Position, GL20.GL_TRIANGLES);

                for (int i = 0; i < MESH_SIZE + 1; i++) {
                    for (int j = 0; j < MESH_SIZE + 1; j++) {
                        x.set(i + width, heightMap[i + width * MESH_SIZE][j + 1 + height * MESH_SIZE], j + 1).add(width * MESH_SIZE, 0f, height * MESH_SIZE);
                        y.set(i + 1 + width, heightMap[i + 1 + width * MESH_SIZE][j + height * MESH_SIZE], j).add(width * MESH_SIZE, 0f, height * MESH_SIZE);
                        z.set(i + width, heightMap[i + width * MESH_SIZE][j + height * MESH_SIZE], j).add(width * MESH_SIZE, 0f, height * MESH_SIZE);
                        meshBuilder.triangle(x, y, z);

                        x.set(i + 1 + width, heightMap[i + 1 + width * MESH_SIZE][j + 1 + height * MESH_SIZE], j + 1).add(width * MESH_SIZE, 0f, height * MESH_SIZE);
                        y.set(i + 1 + width, heightMap[i + 1 + width * MESH_SIZE][j + height * MESH_SIZE], j).add(width * MESH_SIZE, 0f, height * MESH_SIZE);
                        z.set(i + width, heightMap[i + width * MESH_SIZE][j + 1 + height * MESH_SIZE], j + 1).add(width * MESH_SIZE, 0f, height * MESH_SIZE);
                        meshBuilder.triangle(x, y, z);
                    }
                }

                meshList.add(meshBuilder.end());
            }
        }

        return getModelFromMesh(meshList);
    }

    public void createHeightMap() {
        heightMap[0][0] = 15;

        for (int i = 1; i < terrainSize * MESH_SIZE + 2; i++) {
            int heightChange = MathUtils.random(2);
            int previousHorizontalHeight = heightMap[i - 1][0];
            int previousVerticalHeight = heightMap[0][i - 1];

            if (heightChange == 0) {
                heightMap[i][0] = previousHorizontalHeight - 1;
            } else if (heightChange == 1) {
                heightMap[i][0] = previousHorizontalHeight;
            } else {
                heightMap[i][0] = previousHorizontalHeight + 1;
            }

            heightChange = MathUtils.random(2);

            if (heightChange == 0) {
                heightMap[0][i] = previousVerticalHeight - 1;
            } else if (heightChange == 1) {
                heightMap[0][i] = previousVerticalHeight;
            } else {
                heightMap[0][i] = previousVerticalHeight + 1;
            }
        }

        for (int i = 1; i < terrainSize * MESH_SIZE + 2; i++) {
            for (int j = 1; j < terrainSize * MESH_SIZE + 2; j++) {
                int heightDif = Math.abs(heightMap[i - 1][j] - heightMap[i][j - 1]);

                if (heightDif >= 2) {
                    heightMap[i][j] = (heightMap[i - 1][j] + heightMap[i][j - 1]) / 2;
                } else {
                    int heightChange = MathUtils.random(1);

                    if (heightChange == 0) {
                        heightMap[i][j] = heightMap[i - 1][j];
                    } else {
                        heightMap[i][j] = heightMap[i][j - 1];
                    }
                }
            }
        }
    }

    private Model getModelFromMesh(List<Mesh> meshList) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        Material soilMaterial = new Material(TextureAttribute.createDiffuse(new Texture("core/assets/soil_texture.jpg")));

        int count = 0;

        for (Mesh mesh : meshList) {
            modelBuilder.part("Terrain" + count, mesh, GL20.GL_LINES, soilMaterial);
//            modelBuilder.part("Terrain" + count, mesh, GL20.GL_TRIANGLES, soilMaterial);
        }

        return modelBuilder.end();
    }

    public void buildTerrainFromHeightmap() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("core/assets/heightmap.png"));

        int width = 4096;
        int height = 4096;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = new Color();
                Color.rgba8888ToColor(color, pixmap.getPixel(i, j));
                heightMap[i][j] = (int) (256 - color.r * 255);
            }
        }

        Color color = new Color();
        Color.rgba8888ToColor(color, pixmap.getPixel(0, 0));
        


        System.out.println(heightMap[0][0]);

//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                System.out.print(heightMap[i][j]);
//            }
//
//            System.out.println();
//        }
    }
}
