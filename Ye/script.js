// Global variables and setup
let scene, camera, renderer, controls;
let clock = new THREE.Clock();
let gameState = "menu";
let score = 0;
let health = 100;
let gameOver = false;
let zombies = [];
let bullets = [];
let particleSystem;
let gunshotSound, zombieHitSound;

// Player object
let player = {
  mesh: null,
  speed: 5,
  jumpStrength: 10,
  velocity: new THREE.Vector3(0, 0, 0),
  momentum: new THREE.Vector3(0, 0, 0),
  onGround: false,
  sprintMultiplier: 1.8,
  frictionCoefficient: 0.8 // Lower values = more slippery
};
// Constants
const GRAVITY = 20;
let zombieSpawnInterval = 5;
let zombieTimer = 0;
const maxZombies = 20;
const raycaster = new THREE.Raycaster();
const mouse = new THREE.Vector2(0, 0);

// DOM elements
const overlay = document.getElementById("overlay");
const startButton = document.getElementById("startButton");
const scoreDisplay = document.getElementById("score");
const healthDisplay = document.getElementById("health");

// Weapon system
let currentWeapon = "pistol";
const weapons = {
  pistol: { damage: 100, fireRate: 0.5, ammo: Infinity, cooldown: 0 },
  shotgun: { damage: 200, fireRate: 1.0, ammo: 20, cooldown: 0 },
  rifle: { damage: 150, fireRate: 0.3, ammo: 30, cooldown: 0 }
};

// Wave system
let wave = 1;
let waveTimer = 0;
const waveInterval = 30;

// Initialize the game
function init() {
  // Set up Three.js scene
  scene = new THREE.Scene();
  scene.background = new THREE.Color(0x222222);
  
  // Create fog for atmosphere
  scene.fog = new THREE.FogExp2(0x222222, 0.005);
  
  // Set up camera
  camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 3000);
  camera.position.set(0, 10, 0);
  
  // Set up renderer
  renderer = new THREE.WebGLRenderer({ antialias: true, canvas: document.getElementById("gameCanvas") });
  renderer.setSize(window.innerWidth, window.innerHeight);
  renderer.setPixelRatio(window.devicePixelRatio);
  renderer.shadowMap.enabled = true;
  
  // Add lights
  let ambientLight = new THREE.AmbientLight(0x404040, 0.5);
  scene.add(ambientLight);
  
  let directionalLight = new THREE.DirectionalLight(0xffffff, 1);
  directionalLight.position.set(50, 100, 50);
  directionalLight.castShadow = true;
  directionalLight.shadow.mapSize.width = 2048;
  directionalLight.shadow.mapSize.height = 2048;
  scene.add(directionalLight);
  
  // Add point lights for atmosphere
  const pointLight1 = new THREE.PointLight(0xff4500, 1, 100);
  pointLight1.position.set(50, 20, 50);
  scene.add(pointLight1);
  
  const pointLight2 = new THREE.PointLight(0x4169e1, 1, 100);
  pointLight2.position.set(-50, 20, -50);
  scene.add(pointLight2);

  // Setup third-person controls
  startButton.addEventListener("click", () => {
    overlay.style.display = "none";
    if (gameState === "menu") {
      gameState = "playing";
    }
  });
  
  // Initialize game components
  initPlayer();
  createEnvironment();
  initParticleSystem();
  loadAudio();
  
  // Add event listeners
  window.addEventListener("resize", onWindowResize, false);
  document.addEventListener("mousedown", onMouseDown);
  document.addEventListener("keydown", onKeyDown);
  
  // Set up key state tracking
  setupKeyControls();
  
  // Spawn initial zombies
  for (let i = 0; i < 5; i++) {
    spawnZombie();
  }
}

function setupKeyControls() {
  // Key states for smooth movement
  window.keyStates = {};
  window.addEventListener("keydown", (e) => {
    keyStates[e.key.toLowerCase()] = true;
    
    // Weapon switching
    if (e.key === "1") switchWeapon("pistol");
    if (e.key === "2") switchWeapon("shotgun");
    if (e.key === "3") switchWeapon("rifle");
  });
  window.addEventListener("keyup", (e) => {
    keyStates[e.key.toLowerCase()] = false;
  });
  window.addEventListener("keydown", (e) => {
    keyStates[e.key.toLowerCase()] = true;
    
    // Weapon switching
    if (e.key === "1") switchWeapon("pistol");
    if (e.key === "2") switchWeapon("shotgun");
    if (e.key === "3") switchWeapon("rifle");
    
    // Build momentum when sprinting starts
    if (e.key.toLowerCase() === "shift" && player.onGround) {
      let moveDir = new THREE.Vector3();
      if (keyStates["w"] || keyStates["arrowup"]) moveDir.z -= 1;
      if (keyStates["s"] || keyStates["arrowdown"]) moveDir.z += 1;
      if (keyStates["a"] || keyStates["arrowleft"]) moveDir.x -= 1;
      if (keyStates["d"] || keyStates["arrowright"]) moveDir.x += 1;
      
      if (moveDir.length() > 0) {
        // Get movement direction relative to camera
        moveDir.normalize();
        let cameraDirection = new THREE.Vector3();
        camera.getWorldDirection(cameraDirection);
        cameraDirection.y = 0;
        cameraDirection.normalize();
        
        let cameraRight = new THREE.Vector3(1, 0, 0);
        cameraRight.applyQuaternion(camera.quaternion);
        cameraRight.y = 0;
        cameraRight.normalize();
        
        let move = new THREE.Vector3()
          .addScaledVector(cameraDirection, -moveDir.z)
          .addScaledVector(cameraRight, moveDir.x);
        
        // Add to existing momentum
        player.momentum.add(move.multiplyScalar(player.speed * 0.2));
      }
    }
  });
}

function initPlayer() {
  // Create player mesh
  const textureLoader = new THREE.TextureLoader();
  const playerTexture = textureLoader.load("kanye.png", (texture) => {
    material.map = texture;
    material.needsUpdate = true;
  }, undefined, (err) => {
    console.error("Error loading player texture:", err);
  });
  
  let geometry = new THREE.BoxGeometry(2, 4, 2);
  let material = new THREE.MeshBasicMaterial({ color: 0xffffff });
  player.mesh = new THREE.Mesh(geometry, material);
  player.mesh.position.set(0, 10, 0);
  player.mesh.castShadow = true;
  player.mesh.receiveShadow = true;
  scene.add(player.mesh);
  
  // Third person camera setup
  camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 3000);
  camera.position.set(0, 15, 10); // Position behind and above player
  
  // Create orbital controls for third person
  controls = new THREE.OrbitControls(camera, renderer.domElement);
  controls.target.set(0, 10, 0);
  controls.enableZoom = false;
  controls.minDistance = 5;
  controls.maxDistance = 15;
  controls.enablePan = false;
  controls.enableDamping = true;
  controls.dampingFactor = 0.05;
  
  // Initial camera update
  controls.update();
}
function createEnvironment() {
  // Create floor
  let floorGeometry = new THREE.PlaneGeometry(3000, 3000, 50, 50);
  let floorMaterial = new THREE.MeshPhongMaterial({ 
    color: 0x333333,
    roughness: 0.8,
    metalness: 0.2
  });
  let floor = new THREE.Mesh(floorGeometry, floorMaterial);
  floor.rotation.x = -Math.PI / 2;
  floor.position.y = 0;
  floor.receiveShadow = true;
  scene.add(floor);
  
  // Add random obstacles/buildings
  for (let i = 0; i < 50; i++) {
    let buildingGeometry = new THREE.BoxGeometry(
      5 + Math.random() * 20,
      5 + Math.random() * 25,
      5 + Math.random() * 20
    );
    
    let buildingMaterial = new THREE.MeshPhongMaterial({
      color: new THREE.Color(
        0.2 + Math.random() * 0.2,
        0.2 + Math.random() * 0.2,
        0.2 + Math.random() * 0.2
      )
    });
    
    let building = new THREE.Mesh(buildingGeometry, buildingMaterial);
    
    // Position buildings randomly, but away from the player start position
    let distance;
    do {
      building.position.x = (Math.random() - 0.5) * 500;
      building.position.z = (Math.random() - 0.5) * 500;
      building.position.y = buildingGeometry.parameters.height / 2;
      
      distance = Math.sqrt(
        building.position.x * building.position.x + 
        building.position.z * building.position.z
      );
    } while (distance < 20); // Ensure buildings aren't too close to player spawn
    
    building.castShadow = true;
    building.receiveShadow = true;
    scene.add(building);
  }
}

function initParticleSystem() {
  // Create particle system for blood effects
  const particleCount = 1000;
  const particleGeometry = new THREE.BufferGeometry();
  const particlePositions = new Float32Array(particleCount * 3);
  const particleSizes = new Float32Array(particleCount);
  const particleColors = new Float32Array(particleCount * 3);
  const particleLifetime = new Float32Array(particleCount);
  
  // Initialize all particles as inactive
  for (let i = 0; i < particleCount; i++) {
    particlePositions[i * 3] = 0;
    particlePositions[i * 3 + 1] = 0;
    particlePositions[i * 3 + 2] = 0;
    particleSizes[i] = 0.1;
    particleColors[i * 3] = 1;     // R - red for blood
    particleColors[i * 3 + 1] = 0; // G
    particleColors[i * 3 + 2] = 0; // B
    particleLifetime[i] = 0;       // Inactive
  }
  
  particleGeometry.setAttribute('position', new THREE.BufferAttribute(particlePositions, 3));
  particleGeometry.setAttribute('size', new THREE.BufferAttribute(particleSizes, 1));
  particleGeometry.setAttribute('color', new THREE.BufferAttribute(particleColors, 3));
  particleGeometry.setAttribute('lifetime', new THREE.BufferAttribute(particleLifetime, 1));
  
  // Particle shader material
  const particleMaterial = new THREE.ShaderMaterial({
    uniforms: {
      time: { value: 0 }
    },
    vertexShader: `
      attribute float size;
      attribute vec3 color;
      attribute float lifetime;
      varying vec3 vColor;
      varying float vLifetime;
      
      void main() {
        vColor = color;
        vLifetime = lifetime;
        
        // Only show if lifetime > 0
        vec4 mvPosition = modelViewMatrix * vec4(position, 1.0);
        gl_PointSize = size * (100.0 / length(mvPosition.xyz));
        gl_Position = projectionMatrix * mvPosition;
      }
    `,
    fragmentShader: `
      varying vec3 vColor;
      varying float vLifetime;
      
      void main() {
        if (vLifetime <= 0.0) discard;
        
        // Make particles round
        float dist = length(gl_PointCoord - vec2(0.5, 0.5));
        if (dist > 0.5) discard;
        
        // Fade out as lifetime approaches 0
        gl_FragColor = vec4(vColor, vLifetime);
      }
    `,
    transparent: true,
    depthWrite: false,
    blending: THREE.AdditiveBlending
  });
  
  particleSystem = new THREE.Points(particleGeometry, particleMaterial);
  particleSystem.userData.velocities = [];
  scene.add(particleSystem);
}

function loadAudio() {
  // Create audio context
  const AudioContext = window.AudioContext || window.webkitAudioContext;
  window.audioContext = new AudioContext();
  
  // Load sound effects
  gunshotSound = new Audio("gunshot.mp3");
  zombieHitSound = new Audio("zombieHit.mp3");
  
  // Preload sounds
  gunshotSound.load();
  zombieHitSound.load();
}

function onWindowResize() {
  camera.aspect = window.innerWidth / window.innerHeight;
  camera.updateProjectionMatrix();
  renderer.setSize(window.innerWidth, window.innerHeight);
}

function onMouseDown(event) {
  if (gameState === "playing" && !gameOver) {
    if (event.button === 0) { // Left click
      const weapon = weapons[currentWeapon];
      
      // Check weapon cooldown
      if (weapon.cooldown <= 0) {
        if (currentWeapon === "shotgun") {
          // Shotgun fires multiple bullets in a spread
          for (let i = 0; i < 5; i++) {
            shootBullet(i * 0.05 - 0.1);
          }
        } else {
          shootBullet(0);
        }
        
        // Apply cooldown based on fire rate
        weapon.cooldown = weapon.fireRate;
        
        // Decrease ammo if not infinite
        if (weapon.ammo !== Infinity) {
          weapon.ammo--;
          if (weapon.ammo <= 0) {
            switchWeapon("pistol");
          }
        }
      }
    }
  }
}

function onKeyDown(event) {
  // Restart game when pressing 'R' at game over screen
  if (event.key.toLowerCase() === 'r' && gameOver) {
    restartGame();
  }
}

function shootBullet(spreadAngle = 0) {
  // Create bullet geometry
  let bulletGeometry = new THREE.SphereGeometry(0.25, 8, 8);
  let bulletMaterial = new THREE.MeshBasicMaterial({ 
    color: getWeaponColor(currentWeapon)
  });
  
  let bullet = new THREE.Mesh(bulletGeometry, bulletMaterial);
  
  // Position bullet at player with offset
  let bulletSpawnPos = player.mesh.position.clone();
  bulletSpawnPos.y += 1.5; // Adjust for player height
  bullet.position.copy(bulletSpawnPos);
  
  // Get shooting direction from player's facing direction
  let direction = new THREE.Vector3(0, 0, -1).applyQuaternion(player.mesh.quaternion);
  
  // Apply spread angle for shotgun
  if (spreadAngle !== 0) {
    let perpVector = new THREE.Vector3(1, 0, 0).applyQuaternion(player.mesh.quaternion);
    direction.add(perpVector.multiplyScalar(spreadAngle));
    direction.normalize();
  }
  
  // Set bullet properties
  bullet.velocity = direction.multiplyScalar(50);
  bullet.damage = weapons[currentWeapon].damage;
  bullet.alive = true;
  
  // Add light to bullet for visual effect
  let bulletLight = new THREE.PointLight(getWeaponColor(currentWeapon), 1, 10);
  bullet.add(bulletLight);
  
  scene.add(bullet);
  bullets.push(bullet);
  
  // Play sound effect
  playSound(gunshotSound);
}

function getWeaponColor(weaponName) {
  switch(weaponName) {
    case "pistol": return 0xffff00; // Yellow
    case "shotgun": return 0xff6600; // Orange
    case "rifle": return 0x00ffff; // Cyan
    default: return 0xffffff; // White
  }
}

function createZombie(x, z) {
  // Create zombie mesh
  const textureLoader = new THREE.TextureLoader();
  const zombieTexture = textureLoader.load("zombie.png", (texture) => {
    material.map = texture;
    material.needsUpdate = true;
  }, undefined, (err) => {
    console.error("Error loading zombie texture:", err);
  });
  
  let geometry = new THREE.BoxGeometry(2, 4, 2);
  let material = new THREE.MeshPhongMaterial({ 
    color: 0x00ff00,
    roughness: 0.7,
    metalness: 0.3
  });
  
  let zombie = new THREE.Mesh(geometry, material);
  zombie.position.set(x, 2, z);
  zombie.castShadow = true;
  zombie.receiveShadow = true;
  
  // Zombie properties
  zombie.userData = { 
    speed: 0.02 + Math.random() * 0.02 * (wave * 0.1),
    health: 100,
    damage: 5 + wave,
    attackCooldown: 0
  };
  
  scene.add(zombie);
  return zombie;
}

function spawnZombie() {
  // Spawn zombie at a distance from player
  let angle = Math.random() * Math.PI * 2;
  let distance = 50 + Math.random() * 100;
  let playerPos = player.mesh.position;
  let x = playerPos.x + Math.cos(angle) * distance;
  let z = playerPos.z + Math.sin(angle) * distance;
  
  let zombie = createZombie(x, z);
  
  // Apply difficulty modifiers
  const diffSettings = DIFFICULTY_SETTINGS[currentDifficulty];
  zombie.userData.speed *= diffSettings.zombieSpeed * (1 + (wave * 0.05));
  zombie.userData.health *= diffSettings.zombieHealth * (1 + (wave * 0.1));
  zombie.userData.damage *= diffSettings.zombieDamage * (1 + (wave * 0.05));
  
  zombies.push(zombie);
}

function switchWeapon(name) {
  if (weapons[name] && weapons[name].ammo > 0) {
    currentWeapon = name;
    // Update HUD to show weapon change
    updateHUD();
  }
}

function updatePlayerPhysics(delta) {
  // Movement direction based on key presses
  let moveDir = new THREE.Vector3();
  if (keyStates["w"] || keyStates["arrowup"]) moveDir.z -= 1;
  if (keyStates["s"] || keyStates["arrowdown"]) moveDir.z += 1;
  if (keyStates["a"] || keyStates["arrowleft"]) moveDir.x -= 1;
  if (keyStates["d"] || keyStates["arrowright"]) moveDir.x += 1;
  
  // Normalize movement direction
  if (moveDir.length() > 0) {
    moveDir.normalize();
    
    // Get camera direction
    let cameraDirection = new THREE.Vector3();
    camera.getWorldDirection(cameraDirection);
    cameraDirection.y = 0;
    cameraDirection.normalize();
    
    // Get camera right vector
    let cameraRight = new THREE.Vector3(1, 0, 0);
    cameraRight.applyQuaternion(camera.quaternion);
    cameraRight.y = 0;
    cameraRight.normalize();
    
    // Calculate movement vector relative to camera
    let move = new THREE.Vector3()
      .addScaledVector(cameraDirection, -moveDir.z)
      .addScaledVector(cameraRight, moveDir.x);
    
    // Apply movement speed, sprint modifier, and delta
    let speedModifier = keyStates["shift"] ? player.sprintMultiplier : 1;
    move.multiplyScalar(player.speed * speedModifier * delta);
    
    // Apply movement to player
    player.mesh.position.add(move);
    
    // Rotate player mesh to face movement direction
    if (move.length() > 0) {
      player.mesh.lookAt(player.mesh.position.clone().add(move));
    }
  }
  
  // Jumping
  if ((keyStates[" "] || keyStates["space"]) && player.onGround) {
    player.velocity.y = player.jumpStrength;
    player.onGround = false;
  }
  
  // Apply gravity
  player.velocity.y -= GRAVITY * delta;
  
  // Apply momentum for slippery movement
  if (player.momentum.length() > 0) {
    // Apply momentum with decay
    player.mesh.position.add(player.momentum.clone().multiplyScalar(delta));
    player.momentum.multiplyScalar(1 - player.frictionCoefficient * delta);
    
    // Cut off very small momentum values
    if (player.momentum.length() < 0.01) {
      player.momentum.set(0, 0, 0);
    }
  }
  
  // Apply vertical velocity
  player.mesh.position.y += player.velocity.y * delta;
  
  // Floor collision
  if (player.mesh.position.y < 2) {
    player.mesh.position.y = 2;
    player.velocity.y = 0;
    player.onGround = true;
  }
  
  // Update camera target to follow player
  controls.target.copy(player.mesh.position.clone().add(new THREE.Vector3(0, 2, 0)));
  controls.update();
  
  // Update camera position to follow player
  camera.position.copy(player.mesh.position.clone().add(new THREE.Vector3(0, 5, 10)));
}

function updateZombies(delta) {
  for (let i = zombies.length - 1; i >= 0; i--) {
    let zombie = zombies[i];
    
    // Calculate direction to player
    let direction = new THREE.Vector3().subVectors(
      controls.getObject().position,
      zombie.position
    );
    let dist = direction.length();
    
    // Move towards player
    if (dist > 0) {
      direction.normalize();
      zombie.position.add(direction.multiplyScalar(zombie.userData.speed * delta * 60));
      
      // Make zombie face the player
      zombie.lookAt(controls.getObject().position);
    }
    
    // Attack player if close enough
    if (dist < 3) {
      zombie.userData.attackCooldown -= delta;
      
      if (zombie.userData.attackCooldown <= 0) {
        // Deal damage to player
        health -= zombie.userData.damage;
        
        // Reset attack cooldown
        zombie.userData.attackCooldown = 1;
        
        // Visual feedback for being hit
        renderer.domElement.style.boxShadow = "0 0 50px #ff0000 inset";
        setTimeout(() => {
          renderer.domElement.style.boxShadow = "none";
        }, 100);
        
        if (health <= 0) {
          health = 0;
          gameOver = true;
        }
      }
    }
    
    // Add a bobbing motion to zombies for more lifelike movement
    zombie.position.y = 2 + Math.sin(Date.now() * 0.003 + i) * 0.1;
  }
}

function updateBullets(delta) {
  for (let i = bullets.length - 1; i >= 0; i--) {
    let bullet = bullets[i];
    
    // Move bullet
    bullet.position.add(bullet.velocity.clone().multiplyScalar(delta));
    
    // Remove bullet if it goes too far
    if (bullet.position.distanceTo(controls.getObject().position) > 500) {
      scene.remove(bullet);
      bullets.splice(i, 1);
    }
  }
}

function checkCollisions() {
  // Check bullet-zombie collisions
  for (let i = bullets.length - 1; i >= 0; i--) {
    let bullet = bullets[i];
    
    for (let j = zombies.length - 1; j >= 0; j--) {
      let zombie = zombies[j];
      
      if (bullet.position.distanceTo(zombie.position) < 2) {
        // Damage zombie
        zombie.userData.health -= bullet.damage;
        
        // Remove bullet
        scene.remove(bullet);
        bullets.splice(i, 1);
        
        // Spawn blood particles
        spawnBloodParticles(zombie.position.x, zombie.position.y, zombie.position.z);
        
        // Check if zombie is killed
        if (zombie.userData.health <= 0) {
          // Remove zombie
          scene.remove(zombie);
          zombies.splice(j, 1);
          
          // Award points
          score += 100 * wave;
          
          // Play sound
          playSound(zombieHitSound);
          
          // Spawn more zombies if below max
          if (zombies.length < maxZombies) {
            spawnZombie();
          }
        }
        
        break;
      }
    }
  }
}

function spawnBloodParticles(x, y, z) {
  const particleCount = 30;
  const positions = particleSystem.geometry.attributes.position.array;
  const sizes = particleSystem.geometry.attributes.size.array;
  const lifetimes = particleSystem.geometry.attributes.lifetime.array;
  
  // Find inactive particles and activate them
  let activatedCount = 0;
  for (let i = 0; i < positions.length / 3 && activatedCount < particleCount; i++) {
    if (lifetimes[i] <= 0) {
      // Position
      positions[i * 3] = x;
      positions[i * 3 + 1] = y;
      positions[i * 3 + 2] = z;
      
      // Velocity (stored in a separate array since it's not a shader attribute)
      if (!particleSystem.userData.velocities) {
        particleSystem.userData.velocities = [];
      }
      
      particleSystem.userData.velocities[i] = new THREE.Vector3(
        (Math.random() - 0.5) * 10,
        Math.random() * 10,
        (Math.random() - 0.5) * 10
      );
      
      // Size
      sizes[i] = 0.5 + Math.random() * 1.5;
      
      // Lifetime
      lifetimes[i] = 1.0;
      
      activatedCount++;
    }
  }
  
  // Mark attributes for update
  particleSystem.geometry.attributes.position.needsUpdate = true;
  particleSystem.geometry.attributes.size.needsUpdate = true;
  particleSystem.geometry.attributes.lifetime.needsUpdate = true;
}

function updateParticles(delta) {
  if (!particleSystem || !particleSystem.userData.velocities) return;
  
  const positions = particleSystem.geometry.attributes.position.array;
  const lifetimes = particleSystem.geometry.attributes.lifetime.array;
  const velocities = particleSystem.userData.velocities;
  
  // Update all active particles
  for (let i = 0; i < lifetimes.length; i++) {
    if (lifetimes[i] > 0) {
      // Apply velocity
      positions[i * 3] += velocities[i].x * delta;
      positions[i * 3 + 1] += velocities[i].y * delta;
      positions[i * 3 + 2] += velocities[i].z * delta;
      
      // Apply gravity to y velocity
      velocities[i].y -= 9.8 * delta;
      
      // Decrease lifetime
      lifetimes[i] -= delta;
    }
  }
  
  // Mark attributes for update
  particleSystem.geometry.attributes.position.needsUpdate = true;
  particleSystem.geometry.attributes.lifetime.needsUpdate = true;
  
  // Update shader time uniform
  particleSystem.material.uniforms.time.value += delta;
}

function updateZombieWaves(delta) {
  // Get difficulty settings
  const diffSettings = DIFFICULTY_SETTINGS[currentDifficulty];
  
  // Spawn new zombies periodically
  zombieTimer += delta;
  if (zombieTimer > zombieSpawnInterval / diffSettings.zombieSpawnRate && zombies.length < maxZombies) {
    spawnZombie();
    zombieTimer = 0;
  }
  
  // Progress to next wave
  waveTimer += delta;
  if (waveTimer >= waveInterval) {
    wave++;
    waveTimer = 0;
    
    // Increase difficulty with each wave
    zombieSpawnInterval = Math.max(1, zombieSpawnInterval * 0.9);
    
    // Spawn more zombies for new wave
    const zombiesToSpawn = Math.min(5 + wave, maxZombies - zombies.length);
    for (let i = 0; i < zombiesToSpawn; i++) {
      spawnZombie();
    }
    
    // Display wave notification
    showWaveNotification(wave);
  }
}

function showWaveNotification(waveNumber) {
  // Create wave notification element if it doesn't exist
  let notification = document.getElementById("waveNotification");
  if (!notification) {
    notification = document.createElement("div");
    notification.id = "waveNotification";
    notification.style.position = "absolute";
    notification.style.top = "50%";
    notification.style.left = "50%";
    notification.style.transform = "translate(-50%, -50%)";
    notification.style.color = "#ff0000";
    notification.style.fontSize = "3em";
    notification.style.fontWeight = "bold";
    notification.style.textShadow = "0 0 10px #000";
    notification.style.zIndex = "5";
    notification.style.pointerEvents = "none";
    document.body.appendChild(notification);
  }
  
  // Show wave notification
  notification.textContent = `WAVE ${waveNumber}`;
  notification.style.display = "block";
  notification.style.opacity = "1";
  
  // Fade out after 2 seconds
  setTimeout(() => {
    notification.style.transition = "opacity 1s";
    notification.style.opacity = "0";
    setTimeout(() => {
      notification.style.display = "none";
    }, 1000);
  }, 2000);
}

function updateWeaponCooldowns(delta) {
  // Update weapon cooldowns
  for (const weapon in weapons) {
    if (weapons[weapon].cooldown > 0) {
      weapons[weapon].cooldown -= delta;
    }
  }
}

function updateHUD() {
  // Update score and health displays
  scoreDisplay.textContent = `Score: ${score} | Wave: ${wave}`;
  healthDisplay.textContent = `Health: ${Math.floor(health)} | Weapon: ${currentWeapon.toUpperCase()} (${weapons[currentWeapon].ammo === Infinity ? "∞" : weapons[currentWeapon].ammo})`;
}

function playSound(sound) {
  if (sound) {
    // Reset and play sound
    sound.currentTime = 0;
    
    // Start audio context on first interaction
    if (window.audioContext && window.audioContext.state === "suspended") {
      window.audioContext.resume();
    }
    
    sound.play().catch(error => {
      console.error("Error playing sound:", error);
    });
  }
}

function displayGameOver() {
  // Create game over screen
  let overlayDiv = document.getElementById("gameOverScreen");
  if (!overlayDiv) {
    overlayDiv = document.createElement("div");
    overlayDiv.id = "gameOverScreen";
    overlayDiv.innerHTML = `
      <h1>GAME OVER</h1>
      <p>Final Score: ${score}</p>
      <p>Waves Survived: ${wave}</p>
      <p>Press 'R' to restart</p>
    `;
    overlayDiv.style.display = "flex";
    document.body.appendChild(overlayDiv);
  } else {
    overlayDiv.innerHTML = `
      <h1>GAME OVER</h1>
      <p>Final Score: ${score}</p>
      <p>Waves Survived: ${wave}</p>
      <p>Press 'R' to restart</p>
    `;
    overlayDiv.style.display = "flex";
  }
  
  // Release pointer lock
  controls.unlock();
}

function restartGame() {
  // Reset game variables
  score = 0;
  health = 100;
  gameOver = false;
  gameState = "menu";
  wave = 1;
  waveTimer = 0;
  zombieTimer = 0;
  zombieSpawnInterval = 5;
  
  // Reset player
  player.velocity.set(0, 0, 0);
  player.momentum.set(0, 0, 0);
  player.mesh.position.set(0, 2, 0);
  
  // Clear zombies and bullets
  for (let zombie of zombies) {
    scene.remove(zombie);
  }
  zombies = [];
  
  for (let bullet of bullets) {
    scene.remove(bullet);
  }
  bullets = [];
  
  // Reset weapons
  currentWeapon = "pistol";
  weapons.shotgun.ammo = 20;
  weapons.rifle.ammo = 30;
  
  // Hide game over screen
  let gameOverScreen = document.getElementById("gameOverScreen");
  if (gameOverScreen) {
    gameOverScreen.style.display = "none";
  }
  
  // Show start menu
  overlay.style.display = "flex";
  
  // Spawn initial zombies
  for (let i = 0; i < 5; i++) {
    spawnZombie();
  }
}

// Game loop
let lastTime = performance.now();
function animate() {
  requestAnimationFrame(animate);
  
  const currentTime = performance.now();
  const delta = Math.min(0.1, (currentTime - lastTime) / 1000); // Cap delta at 0.1
  lastTime = currentTime;
  
  if (gameState === "playing" && !gameOver) {
    updatePlayerPhysics(delta);
    updateZombies(delta);
    updateBullets(delta);
    updateZombieWaves(delta);
    updateWeaponCooldowns(delta);
    checkCollisions();
    updateHUD();
    updateParticles(delta);
  }
  
  renderer.render(scene, camera);
  
  if (gameOver) {
    displayGameOver();
  }
}

// Start button event listener
startButton.addEventListener("click", () => {
  controls.lock();
});

// Initialize and start the game
init();
animate();

// Add powerup system
let powerups = [];
const POWERUP_TYPES = {
  HEALTH: { color: 0xff0000, effect: "health" },
  SPEED: { color: 0x00ff00, effect: "speed" },
  DAMAGE: { color: 0xffff00, effect: "damage" },
  AMMO: { color: 0x0000ff, effect: "ammo" }
};

// Spawn powerups periodically
function spawnPowerup() {
  if (gameState !== "playing" || gameOver) return;
  
  // Random position near player
  const playerPos = controls.getObject().position;
  const angle = Math.random() * Math.PI * 2;
  const distance = 20 + Math.random() * 30;
  const x = playerPos.x + Math.cos(angle) * distance;
  const z = playerPos.z + Math.sin(angle) * distance;
  
  // Select random powerup type
  const types = Object.values(POWERUP_TYPES);
  const type = types[Math.floor(Math.random() * types.length)];
  
  // Create powerup mesh
  const geometry = new THREE.BoxGeometry(1, 1, 1);
  const material = new THREE.MeshPhongMaterial({ 
    color: type.color,
    emissive: type.color,
    emissiveIntensity: 0.5,
    shininess: 100
  });
  
  const powerup = new THREE.Mesh(geometry, material);
  powerup.position.set(x, 1, z);
  powerup.userData = { type: type.effect, spawnTime: Date.now() };
  
  // Add floating animation
  powerup.userData.floatHeight = powerup.position.y;
  
  // Add glowing light
  const light = new THREE.PointLight(type.color, 1, 10);
  light.position.set(0, 0, 0);
  powerup.add(light);
  
  scene.add(powerup);
  powerups.push(powerup);
  
  // Schedule next powerup spawn
  setTimeout(spawnPowerup, 30000 + Math.random() * 30000); // 30-60 seconds
}

// Update powerups in the game loop
function updatePowerups(delta) {
  for (let i = powerups.length - 1; i >= 0; i--) {
    const powerup = powerups[i];
    
    // Floating animation
    powerup.position.y = powerup.userData.floatHeight + Math.sin(Date.now() * 0.003) * 0.5;
    powerup.rotation.y += delta * 2;
    
    // Check for collision with player
    if (powerup.position.distanceTo(controls.getObject().position) < 3) {
      // Apply powerup effect
      applyPowerup(powerup.userData.type);
      
      // Remove powerup
      scene.remove(powerup);
      powerups.splice(i, 1);
      
      // Show powerup notification
      showPowerupNotification(powerup.userData.type);
    }
    
    // Remove powerups after 60 seconds
    if (Date.now() - powerup.userData.spawnTime > 60000) {
      scene.remove(powerup);
      powerups.splice(i, 1);
    }
  }
}

// Apply powerup effects
function applyPowerup(type) {
  switch (type) {
    case "health":
      health = Math.min(100, health + 50);
      break;
    case "speed":
      player.speed *= 1.5;
      setTimeout(() => {
        player.speed /= 1.5;
      }, 15000); // 15 seconds duration
      break;
    case "damage":
      // Temporary damage boost
      const originalDamage = {};
      for (const weapon in weapons) {
        originalDamage[weapon] = weapons[weapon].damage;
        weapons[weapon].damage *= 2;
      }
      setTimeout(() => {
        for (const weapon in weapons) {
          weapons[weapon].damage = originalDamage[weapon];
        }
      }, 15000); // 15 seconds duration
      break;
    case "ammo":
      // Refill all weapons
      weapons.shotgun.ammo = 20;
      weapons.rifle.ammo = 30;
      break;
  }
  
  // Play powerup sound
  const powerupSound = new Audio("powerup.mp3");
  playSound(powerupSound);
}

// Show powerup notification
function showPowerupNotification(type) {
  // Create notification element
  let notification = document.getElementById("powerupNotification");
  if (!notification) {
    notification = document.createElement("div");
    notification.id = "powerupNotification";
    notification.style.position = "absolute";
    notification.style.bottom = "100px";
    notification.style.left = "50%";
    notification.style.transform = "translateX(-50%)";
    notification.style.color = "#ffffff";
    notification.style.fontSize = "1.5em";
    notification.style.fontWeight = "bold";
    notification.style.textShadow = "0 0 10px #000";
    notification.style.zIndex = "5";
    notification.style.pointerEvents = "none";
    notification.style.backgroundColor = "rgba(0, 0, 0, 0.5)";
    notification.style.padding = "10px 20px";
    notification.style.borderRadius = "5px";
    document.body.appendChild(notification);
  }
  
  // Set message based on powerup type
  let message;
  switch (type) {
    case "health": message = "HEALTH BOOST!"; break;
    case "speed": message = "SPEED BOOST!"; break;
    case "damage": message = "DAMAGE BOOST!"; break;
    case "ammo": message = "AMMO REFILLED!"; break;
  }
  
  // Show notification
  notification.textContent = message;
  notification.style.display = "block";
  notification.style.opacity = "1";
  
  // Fade out after 2 seconds
  setTimeout(() => {
    notification.style.transition = "opacity 1s";
    notification.style.opacity = "0";
    setTimeout(() => {
      notification.style.display = "none";
    }, 1000);
  }, 2000);
}

// Add melee attack
function performMeleeAttack() {
  if (gameState !== "playing" || gameOver) return;
  
  // Create visual effect for melee attack
  const geometrySword = new THREE.BoxGeometry(0.2, 2, 0.2);
  const materialSword = new THREE.MeshPhongMaterial({ color: 0xcccccc });
  const sword = new THREE.Mesh(geometrySword, materialSword);
  
  // Position sword in front of player
  sword.position.set(1, -0.5, -2);
  camera.add(sword);
  
  // Animate sword swing
  let swingAngle = 0;
  const swingSpeed = 10;
  const swingAnimation = setInterval(() => {
    swingAngle += 0.1 * swingSpeed;
    sword.rotation.x = Math.sin(swingAngle) * 2;
    
    if (swingAngle >= Math.PI) {
      clearInterval(swingAnimation);
      camera.remove(sword);
    }
  }, 16);
  
  // Check for hits on zombies
  for (let i = zombies.length - 1; i >= 0; i--) {
    const zombie = zombies[i];
    if (zombie.position.distanceTo(controls.getObject().position) < 4) {
      // Damage zombie
      zombie.userData.health -= 50; // Melee damage
      
      // Spawn blood particles
      spawnBloodParticles(zombie.position.x, zombie.position.y, zombie.position.z);
      
      // Check if zombie is killed
      if (zombie.userData.health <= 0) {
        // Remove zombie
        scene.remove(zombie);
        zombies.splice(i, 1);
        
        // Award points
        score += 150 * wave; // More points for melee kill
        
        // Play sound
        playSound(zombieHitSound);
      }
    }
  }
  
  // Play melee sound
  const meleeSound = new Audio("melee.mp3");
  playSound(meleeSound);
}

// Add event listener for melee attack
document.addEventListener("keydown", (e) => {
  if (e.key === "f" && controls.isLocked && gameState === "playing" && !gameOver) {
    performMeleeAttack();
  }
});

// Add minimap
function createMinimap() {
  // Create minimap container
  const minimapContainer = document.createElement("div");
  minimapContainer.id = "minimap";
  minimapContainer.style.position = "absolute";
  minimapContainer.style.bottom = "20px";
  minimapContainer.style.right = "20px";
  minimapContainer.style.width = "200px";
  minimapContainer.style.height = "200px";
  minimapContainer.style.backgroundColor = "rgba(0, 0, 0, 0.5)";
  minimapContainer.style.border = "2px solid white";
  minimapContainer.style.borderRadius = "50%";
  minimapContainer.style.overflow = "hidden";
  document.body.appendChild(minimapContainer);
  
  // Create minimap canvas
  const minimapCanvas = document.createElement("canvas");
  minimapCanvas.width = 200;
  minimapCanvas.height = 200;
  minimapContainer.appendChild(minimapCanvas);
  
  return minimapCanvas;
}

// Update minimap
function updateMinimap(canvas) {
  const ctx = canvas.getContext("2d");
  const width = canvas.width;
  const height = canvas.height;
  
  // Clear canvas
  ctx.clearRect(0, 0, width, height);
  
  // Set center of minimap to player position
  const playerPos = controls.getObject().position;
  const minimapScale = 0.5; // Scale factor for minimap
  
  // Draw zombies
  ctx.fillStyle = "red";
  for (const zombie of zombies) {
    const relX = (zombie.position.x - playerPos.x) * minimapScale + width / 2;
    const relZ = (zombie.position.z - playerPos.z) * minimapScale + height / 2;
    
    // Only draw if within minimap bounds
    if (relX >= 0 && relX <= width && relZ >= 0 && relZ <= height) {
      ctx.beginPath();
      ctx.arc(relX, relZ, 3, 0, Math.PI * 2);
      ctx.fill();
    }
  }
  
  // Draw powerups
  for (const powerup of powerups) {
    const relX = (powerup.position.x - playerPos.x) * minimapScale + width / 2;
    const relZ = (powerup.position.z - playerPos.z) * minimapScale + height / 2;
    
    // Only draw if within minimap bounds
    if (relX >= 0 && relX <= width && relZ >= 0 && relZ <= height) {
      // Set color based on powerup type
      switch (powerup.userData.type) {
        case "health": ctx.fillStyle = "pink"; break;
        case "speed": ctx.fillStyle = "lime"; break;
        case "damage": ctx.fillStyle = "yellow"; break;
        case "ammo": ctx.fillStyle = "cyan"; break;
      }
      
      ctx.beginPath();
      ctx.arc(relX, relZ, 5, 0, Math.PI * 2);
      ctx.fill();
    }
  }
  
  // Draw player (center)
  ctx.fillStyle = "white";
  ctx.beginPath();
  ctx.arc(width / 2, height / 2, 5, 0, Math.PI * 2);
  ctx.fill();
  
  // Draw player direction
  const dirX = Math.sin(camera.rotation.y) * 10;
  const dirZ = Math.cos(camera.rotation.y) * 10;
  ctx.beginPath();
  ctx.moveTo(width / 2, height / 2);
  ctx.lineTo(width / 2 + dirX, height / 2 + dirZ);
  ctx.strokeStyle = "white";
  ctx.lineWidth = 2;
  ctx.stroke();
}

// Create minimap
const minimapCanvas = createMinimap();

// Add minimap update to game loop
function updateGame(delta) {
  updatePlayerPhysics(delta);
  updateZombies(delta);
  updateBullets(delta);
  updateZombieWaves(delta);
  updateWeaponCooldowns(delta);
  updatePowerups(delta);
  checkCollisions();
  updateHUD();
  updateParticles(delta);
  updateMinimap(minimapCanvas);
}

// Update main game loop
function animate() {
  requestAnimationFrame(animate);
  
  const currentTime = performance.now();
  const delta = Math.min(0.1, (currentTime - lastTime) / 1000); // Cap delta at 0.1
  lastTime = currentTime;
  
  if (gameState === "playing" && !gameOver) {
    updateGame(delta);
  }
  
  renderer.render(scene, camera);
  
  if (gameOver) {
    displayGameOver();
  }
}

const DIFFICULTY_SETTINGS = {
  easy: {
    zombieSpeed: 0.6,
    zombieDamage: 0.6,
    zombieHealth: 0.7,
    playerDamage: 1.3,
    zombieSpawnRate: 0.7
  },
  normal: {
    zombieSpeed: 1.0,
    zombieDamage: 1.0,
    zombieHealth: 1.0,
    playerDamage: 1.0,
    zombieSpawnRate: 1.0
  },
  hard: {
    zombieSpeed: 1.4,
    zombieDamage: 1.5,
    zombieHealth: 1.8,
    playerDamage: 0.8,
    zombieSpawnRate: 1.5
  },
  nightmare: {
    zombieSpeed: 2.0,
    zombieDamage: 2.5,
    zombieHealth: 3.0,
    playerDamage: 0.6,
    zombieSpawnRate: 2.0
  }
};

let currentDifficulty = "normal";

// Add difficulty selector to menu
function createDifficultySelector() {
  const difficultyContainer = document.createElement("div");
  difficultyContainer.style.marginTop = "20px";
  
  const difficultyLabel = document.createElement("div");
  difficultyLabel.textContent = "Difficulty:";
  difficultyLabel.style.color = "white";
  difficultyLabel.style.marginBottom = "10px";
  difficultyContainer.appendChild(difficultyLabel);
  
  const difficultySelect = document.createElement("select");
  difficultySelect.id = "difficultySelect";
  difficultySelect.style.padding = "5px 10px";
  difficultySelect.style.backgroundColor = "#333";
  difficultySelect.style.color = "white";
  difficultySelect.style.border = "1px solid #666";
  
  // Add options
  for (const difficulty in DIFFICULTY_SETTINGS) {
    const option = document.createElement("option");
    option.value = difficulty;
    option.textContent = difficulty.charAt(0).toUpperCase() + difficulty.slice(1);
    if (difficulty === currentDifficulty) {
      option.selected = true;
    }
    difficultySelect.appendChild(option);
  }
  
  difficultyContainer.appendChild(difficultySelect);
  
  // Add event listener
  difficultySelect.addEventListener("change", (e) => {
    currentDifficulty = e.target.value;
  });
  
  // Add to overlay
  overlay.appendChild(difficultyContainer);
}

// Apply difficulty settings
function applyDifficultySettings() {
  const settings = DIFFICULTY_SETTINGS[currentDifficulty];
  
  // Apply to existing zombies
  for (const zombie of zombies) {
    zombie.userData.speed *= settings.zombieSpeed;
    zombie.userData.damage *= settings.zombieDamage;
    zombie.userData.health *= settings.zombieHealth;
  }
  
  // Apply to weapons
  for (const weapon in weapons) {
    weapons[weapon].damage *= settings.playerDamage;
  }
}

// Add leaderboard system
const leaderboard = [];

function saveScore() {
  // Prompt for player name
  const playerName = prompt("Enter your name for the leaderboard:", "Player");
  if (playerName) {
    // Add score to leaderboard
    leaderboard.push({
      name: playerName,
      score: score,
      wave: wave,
      difficulty: currentDifficulty,
      date: new Date().toLocaleDateString()
    });
    
    // Sort leaderboard by score
    leaderboard.sort((a, b) => b.score - a.score);
    
    // Keep only top 10 scores
    if (leaderboard.length > 10) {
      leaderboard.length = 10;
    }
    
    // Save to local storage
    localStorage.setItem("zombieShooterLeaderboard", JSON.stringify(leaderboard));
    
    // Show leaderboard
    displayLeaderboard();
  }
}

function loadLeaderboard() {
  // Load from local storage
  const savedLeaderboard = localStorage.getItem("zombieShooterLeaderboard");
  if (savedLeaderboard) {
    const parsed = JSON.parse(savedLeaderboard);
    leaderboard.length = 0;
    leaderboard.push(...parsed);
  }
}

function displayLeaderboard() {
  // Create leaderboard container
  let leaderboardDiv = document.getElementById("leaderboardScreen");
  if (!leaderboardDiv) {
    leaderboardDiv = document.createElement("div");
    leaderboardDiv.id = "leaderboardScreen";
    leaderboardDiv.style.position = "absolute";
    leaderboardDiv.style.top = "50%";
    leaderboardDiv.style.left = "50%";
    leaderboardDiv.style.transform = "translate(-50%, -50%)";
    leaderboardDiv.style.backgroundColor = "rgba(0, 0, 0, 0.8)";
    leaderboardDiv.style.color = "white";
    leaderboardDiv.style.padding = "20px";
    leaderboardDiv.style.borderRadius = "10px";
    leaderboardDiv.style.minWidth = "300px";
    leaderboardDiv.style.zIndex = "100";
    document.body.appendChild(leaderboardDiv);
  }
  
  // Create leaderboard content
  let html = "<h2>Top Scores</h2><table style='width:100%;text-align:left'>";
  html += "<tr><th>Rank</th><th>Name</th><th>Score</th><th>Wave</th><th>Difficulty</th></tr>";
  
  for (let i = 0; i < leaderboard.length; i++) {
    const entry = leaderboard[i];
    html += `<tr>
      <td>${i + 1}</td>
      <td>${entry.name}</td>
      <td>${entry.score}</td>
      <td>${entry.wave}</td>
      <td>${entry.difficulty}</td>
    </tr>`;
  }
  
  html += "</table><button id='closeLeaderboard' style='margin-top:20px;padding:5px 10px'>Close</button>";
  leaderboardDiv.innerHTML = html;
  
  // Add close button event
  document.getElementById("closeLeaderboard").addEventListener("click", () => {
    leaderboardDiv.style.display = "none";
  });
  
  // Show leaderboard
  leaderboardDiv.style.display = "block";
}

// Update game over to include save score
function displayGameOver() {
  // Create game over screen
  let overlayDiv = document.getElementById("gameOverScreen");
  if (!overlayDiv) {
    overlayDiv = document.createElement("div");
    overlayDiv.id = "gameOverScreen";
    overlayDiv.innerHTML = `
      <h1>GAME OVER</h1>
      <p>Final Score: ${score}</p>
      <p>Waves Survived: ${wave}</p>
      <p>Difficulty: ${currentDifficulty}</p>
      <button id="saveScoreBtn">Save Score</button>
      <button id="restartBtn">Restart</button>
    `;
    overlayDiv.style.display = "flex";
    overlayDiv.style.flexDirection = "column";
    overlayDiv.style.alignItems = "center";
    document.body.appendChild(overlayDiv);
    
    // Add button events
    document.getElementById("saveScoreBtn").addEventListener("click", saveScore);
    document.getElementById("restartBtn").addEventListener("click", restartGame);
  } else {
    overlayDiv.innerHTML = `
      <h1>GAME OVER</h1>
      <p>Final Score: ${score}</p>
      <p>Waves Survived: ${wave}</p>
      <p>Difficulty: ${currentDifficulty}</p>
      <button id="saveScoreBtn">Save Score</button>
      <button id="restartBtn">Restart</button>
    `;
    overlayDiv.style.display = "flex";
    
    // Add button events
    document.getElementById("saveScoreBtn").addEventListener("click", saveScore);
    document.getElementById("restartBtn").addEventListener("click", restartGame);
  }
  
  // Release pointer lock
  controls.unlock();
}

// Add leaderboard button to main menu
function addLeaderboardButton() {
  const leaderboardBtn = document.createElement("button");
  leaderboardBtn.textContent = "Leaderboard";
  leaderboardBtn.style.marginTop = "10px";
  leaderboardBtn.addEventListener("click", displayLeaderboard);
  overlay.appendChild(leaderboardBtn);
}

// Initialize additional features
createDifficultySelector();
addLeaderboardButton();
loadLeaderboard();

// Start spawning powerups
setTimeout(spawnPowerup, 30000); // First powerup after 30 seconds

// Mobile controls for touch devices
function setupMobileControls() {
  if (!('ontouchstart' in window)) return; // Only for touch devices
  
  const mobileControlsDiv = document.createElement("div");
  mobileControlsDiv.id = "mobileControls";
  mobileControlsDiv.style.position = "absolute";
  mobileControlsDiv.style.bottom = "20px";
  mobileControlsDiv.style.left = "20px";
  mobileControlsDiv.style.display = "grid";
  mobileControlsDiv.style.gridTemplateColumns = "repeat(3, 60px)";
  mobileControlsDiv.style.gridTemplateRows = "repeat(3, 60px)";
  mobileControlsDiv.style.gap = "5px";
  mobileControlsDiv.style.zIndex = "10";
  document.body.appendChild(mobileControlsDiv);
  
  // Create direction buttons
  const buttonStyles = {
    width: "60px",
    height: "60px",
    backgroundColor: "rgba(255, 255, 255, 0.3)",
    border: "2px solid white",
    borderRadius: "10px",
    fontSize: "24px",
    color: "white",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    userSelect: "none"
  };
  
  // Direction buttons
  const buttons = [
    { id: "upBtn", text: "↑", x: 1, y: 0, key: "w" },
    { id: "leftBtn", text: "←", x: 0, y: 1, key: "a" },
    { id: "downBtn", text: "↓", x: 1, y: 2, key: "s" },
    { id: "rightBtn", text: "→", x: 2, y: 1, key: "d" },
    { id: "jumpBtn", text: "Jump", x: 1, y: 1, key: " " }
  ];
  
  buttons.forEach(btn => {
    const button = document.createElement("div");
    button.id = btn.id;
    button.textContent = btn.text;
    
    // Apply styles
    Object.assign(button.style, buttonStyles);
    button.style.gridColumn = btn.x + 1;
    button.style.gridRow = btn.y + 1;
    
    // Handle touch events
    button.addEventListener("touchstart", () => {
      keyStates[btn.key] = true;
    });
    
    button.addEventListener("touchend", () => {
      keyStates[btn.key] = false;
    });
    
    mobileControlsDiv.appendChild(button);
    // Add sprint button
    const sprintBtn = document.createElement("div");
    sprintBtn.id = "sprintBtn";
    sprintBtn.textContent = "Sprint";

    Object.assign(sprintBtn.style, buttonStyles);
    sprintBtn.style.position = "absolute";
    sprintBtn.style.right = "160px";
    sprintBtn.style.bottom = "20px";
    sprintBtn.style.backgroundColor = "rgba(0, 255, 0, 0.3)";

    sprintBtn.addEventListener("touchstart", () => {
      keyStates["shift"] = true;
    });

    sprintBtn.addEventListener("touchend", () => {
      keyStates["shift"] = false;
    });

    document.body.appendChild(sprintBtn);
  });
  
  // Add shoot button
  const shootBtn = document.createElement("div");
  shootBtn.id = "shootBtn";
  shootBtn.textContent = "🔫";
  
  Object.assign(shootBtn.style, buttonStyles);
  shootBtn.style.position = "absolute";
  shootBtn.style.right = "20px";
  shootBtn.style.bottom = "20px";
  shootBtn.style.backgroundColor = "rgba(255, 0, 0, 0.3)";
  
  shootBtn.addEventListener("touchstart", () => {
    // Simulate mouse click
    onMouseDown({ button: 0 });
  });
  
  document.body.appendChild(shootBtn);
  
  // Add weapon switch button
  const weaponBtn = document.createElement("div");
  weaponBtn.id = "weaponBtn";
  weaponBtn.textContent = "🔄";
  
  Object.assign(weaponBtn.style, buttonStyles);
  weaponBtn.style.position = "absolute";
  weaponBtn.style.right = "90px";
  weaponBtn.style.bottom = "20px";
  
  let currentWeaponIndex = 0;
  const weaponList = ["pistol", "shotgun", "rifle"];
  
  weaponBtn.addEventListener("touchstart", () => {
    currentWeaponIndex = (currentWeaponIndex + 1) % weaponList.length;
    switchWeapon(weaponList[currentWeaponIndex]);
  });
  
  document.body.appendChild(weaponBtn);
}

// Setup mobile controls
setupMobileControls();

// Add graphics quality selector
function createGraphicsSelector() {
  const graphicsContainer = document.createElement("div");
  graphicsContainer.style.marginTop = "20px";
  
  const graphicsLabel = document.createElement("div");
  graphicsLabel.textContent = "Graphics Quality:";
  graphicsLabel.style.color = "white";
  graphicsLabel.style.marginBottom = "10px";
  graphicsContainer.appendChild(graphicsLabel);
  
  const graphicsSelect = document.createElement("select");
  graphicsSelect.id = "graphicsSelect";
  graphicsSelect.style.padding = "5px 10px";
  graphicsSelect.style.backgroundColor = "#333";
  graphicsSelect.style.color = "white";
  graphicsSelect.style.border = "1px solid #666";
  
  // Add options
  const options = ["Low", "Medium", "High"];
  options.forEach(option => {
    const optionEl = document.createElement("option");
    optionEl.value = option.toLowerCase();
    optionEl.textContent = option;
    if (option === "Medium") {
      optionEl.selected = true;
    }
    graphicsSelect.appendChild(optionEl);
  });
  
  graphicsContainer.appendChild(graphicsSelect);
  
  // Add event listener
  graphicsSelect.addEventListener("change", (e) => {
    setGraphicsQuality(e.target.value);
  });
  
  // Add to overlay
  overlay.appendChild(graphicsContainer);
}

// Set graphics quality
function setGraphicsQuality(quality) {
  switch (quality) {
    case "low":
      renderer.setPixelRatio(1);
      renderer.shadowMap.enabled = false;
      scene.fog.density = 0.01;
      break;
    case "medium":
      renderer.setPixelRatio(window.devicePixelRatio);
      renderer.shadowMap.enabled = true;
      scene.fog.density = 0.005;
      break;
    case "high":
      renderer.setPixelRatio(window.devicePixelRatio);
      renderer.shadowMap.enabled = true;
      renderer.shadowMap.type = THREE.PCFSoftShadowMap;
      scene.fog.density = 0.003;
      break;
  }
}

// Add graphics selector
createGraphicsSelector();

// Start the game
console.log("Zombie Shooter Initialized!");