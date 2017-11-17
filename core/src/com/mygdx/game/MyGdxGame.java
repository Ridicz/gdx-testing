package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
	private ModelBatch modelBatch;
	private Environment environment;
	private PerspectiveCamera camera;
	private CameraInputController camController;
	private PointLight pointLight;
	private DirectionalLight directionalLight;

	private ModelInstance instance;
	private ModelInstance human;

	private AssetManager assetManager;

	private AnimationController animationController;

	private float val = 0;

	private ModelInstance terrainModelInstance;

	private List<ModelInstance> terrainList;

	private InputHandler inputHandler;

	@Override
	public void create () {
		DefaultShader.Config config = new DefaultShader.Config();
		config.numBones = 8;
		modelBatch = new ModelBatch(new DefaultShaderProvider(config));

		camera = new PerspectiveCamera(75f, 1600f, 1000f);
		camera.position.set(0f, 10f, 0f);
		camera.lookAt(10f, 10f, 10f);
		camera.far = 1000f;
		camController = new CameraInputController(camera);
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
//		environment.add(pointLight = new PointLight().set(0.8f, 0.8f, 0.8f, 10f, 0f, 0f, 50f));

		directionalLight = new DirectionalLight();
		directionalLight.direction.set(0f, -1f, 0f);
		directionalLight.color.set(1f, 1f, 1f, 1f);
		environment.add(directionalLight);

		assetManager = new AssetManager();
		assetManager.load("core/assets/test.g3db", Model.class);
		assetManager.load("core/assets/human_human.g3db", Model.class);
		assetManager.finishLoading();

		Model model = assetManager.get("core/assets/test.g3db", Model.class);
		instance = new ModelInstance(model);
		instance.transform.scale(0.01f, 0.01f, 0.01f);
//		Gdx.input.setInputProcessor(camController);
		inputHandler = new InputHandler(camera);
		Gdx.input.setInputProcessor(inputHandler);

		Model humanModel = assetManager.get("core/assets/human_human.g3db", Model.class);
		human = new ModelInstance(humanModel);
		human.transform.scale(0.1f, 0.1f, 0.1f);

		animationController = new AnimationController(instance);

		animationController.setAnimation("Armature|ArmatureAction", -1).speed = 2;

//		Model terrainModel = TerrainMeshBuilder.buildTerrainMesh();
//		terrainModelInstance = new ModelInstance(terrainModel);

		terrainList = new ArrayList<ModelInstance>();

		TerrainMeshBuilder terrainMeshBuilder = new TerrainMeshBuilder(64);

//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				Model terrainModel = terrainMeshBuilder.buildTerrainMesh();
//				ModelInstance terrainModelInstance = new ModelInstance(terrainModel);
//				terrainModelInstance.transform.translate(TerrainMeshBuilder.MESH_SIZE * i, 0f, TerrainMeshBuilder.MESH_SIZE * j);
//				terrainList.add(terrainModelInstance);
//			}
//		}

		Model terrainModel = terrainMeshBuilder.buildTerrainMesh();
		terrainModelInstance = new ModelInstance(terrainModel);



//		System.out.println(human.animations.get(0).id);

//		for (Animation animation : human.animations) {
//			System.out.println(animation.id);
//		}

//		animationController.setAnimation("armature|move_run", -1);

		for (Node node : instance.nodes) {
			System.out.println(node.id);
		}
	}

	@Override
	public void resize (int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void render () {
		inputHandler.update();

		animationController.update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		camController.update();
//		pointLight.position.rotate(Vector3.Y, Gdx.graphics.getDeltaTime() * 90f);
		modelBatch.begin(camera);
		modelBatch.render(instance, environment);
		modelBatch.render(terrainModelInstance, environment);
//		modelBatch.render(terrainList, environment);
//		modelBatch.render(human, environment);
		modelBatch.end();
//		instance.transform = instance.transform.rotate(Vector3.Y, 0.1f);
//		instance.transform = instance.transform.translate(2f, 0f, 0f);



//		for (Node node : instance.nodes) {
//			System.out.println(node.id);
//		}

//		instance.getNode("Tiger Turret Main_Turret").globalTransform.rotate(0f, 1f, 0f, 0.3f);
//		instance.getNode("Tiger Turret Machine_Gun").globalTransform.rotate(0f, 1f, 0f, 0.3f);
//		instance.getNode("Tiger Turret Main_Gun").globalTransform.rotate(0f, 1f, 0f, 0.3f);
//		instance.getNode("Tiger Turret Hatch").globalTransform.rotate(0f, 1f, 0f, 0.3f);
//		instance.getNode("Tiger Turret Main_Turret").localTransform.rotate(0f, 1f, 0f, 0.3f);
////		instance.transform.rotate(0f, 1f, 0f, -0.3f);
////		instance.getNode("Tiger Turret Main_Gun").globalTransform.rotate(0f, 1f, 0f, 1f);
//		instance.getNode("Turret").localTransform.rotate(0f, 1f, 0f, 1f);
//
////		instance.getNode("Tiger Turret Main_Gun").translation.rotate(1f, 1f, 1f, 10f);
////		instance.getNode("Tiger Turret Main_Gun").translation.add(0.01f);
//
//		instance.getNode("Armature").getChild(1).getChild(0).getChild(0).localTransform.rotate(0f, 1f, 0f, 0.3f);

//		System.out.println(instance.getNode("Armature").getChild(1).getChild(0).getChild(0).localTransform);

//		instance.getNode("Armature").getChild(1).getChild(0).getChild(0).localTransform.rotate(0f, 1f, 0f, 1f);
//		instance.getNode("Tiger Hull").localTransform.rotate(0f, 1f, 0f, 1f);
//

//		float angles = instance.getNode("Armature").getChild(1).getChild(0).getChild(0).rotation.getAngleAround();

//		System.out.println(yaw);

		instance.getNode("Armature").getChild(1).getChild(0).getChild(0).rotation.setEulerAngles(val, 180f, 90f - 4f);

		val += 0.4;

		instance.calculateTransforms();
	}

	@Override
	public void dispose () {
		terrainModelInstance.model.dispose();
		modelBatch.dispose();
	}
}
