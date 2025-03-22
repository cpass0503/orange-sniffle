// --- SETUP & GLOBAL VARIABLES ---

const canvas = document.getElementById("gameCanvas");
const ctx = canvas.getContext("2d");
const scoreDisplay = document.getElementById("score");
const messageDisplay = document.getElementById("message");

// Level settings
const levelHeight = 2000; // Total level height in pixels
let cameraY = 0;          // Vertical camera offset

// Game state
let score = 0;
let gameOver = false;
let gameWon = false;

// Load images (replace these paths with your actual images)
const playerImg = new Image();
playerImg.src = "https://media.tenor.com/gQrqMKHrmRMAAAAj/kanye-west.gif"; // Kanye image

const bgGermany = new Image();
bgGermany.src = "https://www.bundestag.de/resource/image/217958/16x9/1460/822/ade86fad853892917e47451beafd9699/507EA324B4775F3D9D9083BE142D4C83/german_flag.jpg"; // Background for lower part (Germany)

const bgMusical = new Image();
bgMusical.src = "https://m.media-amazon.com/images/I/91bJjC01qIL._UF1000,1000_QL80_.jpg"; // Background for mid-level (musical area)

const bgMansion = new Image();
bgMansion.src = "https://i.ytimg.com/vi/JEBTjB__im8/maxresdefault.jpg"; // Background for high-level (mansions)

const yeezyImg = new Image();
yeezyImg.src = "https://www.truetosole.hu/cdn/shop/products/cp9652_1_1167x700.png?v=1607072360"; // Yeezy shoe image

// --- PLAYER & PHYSICS ---
const player = {
  x: 100,
  y: levelHeight - 60,  // Start near bottom of level
  width: 40,
  height: 40,
  vx: 0,
  vy: 0,
  speed: 3,
  jumpStrength: -12,
  onGround: false
};

const gravity = 0.5;

// --- INPUT HANDLING (WASD / Arrow Keys) ---
const keys = { left: false, right: false, up: false };

window.addEventListener("keydown", (e) => {
  if (e.key === "ArrowLeft" || e.key.toLowerCase() === "a") keys.left = true;
  if (e.key === "ArrowRight" || e.key.toLowerCase() === "d") keys.right = true;
  if (e.key === "ArrowUp" || e.key.toLowerCase() === "w" || e.key === " ") keys.up = true;
});

window.addEventListener("keyup", (e) => {
  if (e.key === "ArrowLeft" || e.key.toLowerCase() === "a") keys.left = false;
  if (e.key === "ArrowRight" || e.key.toLowerCase() === "d") keys.right = false;
  if (e.key === "ArrowUp" || e.key.toLowerCase() === "w" || e.key === " ") keys.up = false;
});

// --- PLATFORMS ---
// For simplicity, we define an array of platforms (each is an object with x, y, width, height)
// You can add more platforms as needed.
let platforms = [
  // Ground platform at the bottom (y = levelHeight, e.g., 2000)
  { x: 0, y: levelHeight, width: canvas.width, height: 20 },
  // Floating platforms, each 100 pixels higher than the one below
  { x: 150, y: levelHeight - 100, width: 120, height: 20 },
  { x: 400, y: levelHeight - 200, width: 150, height: 20 },
  { x: 200,  y: levelHeight - 300, width: 100, height: 20 },
  { x: 300, y: levelHeight - 400, width: 120, height: 20 },
  { x: 550, y: levelHeight - 500, width: 140, height: 20 },
  { x: 400, y: levelHeight - 600, width: 150, height: 20 },
  { x: 450, y: levelHeight - 700, width: 120, height: 20 },
  { x: 200, y: levelHeight - 800, width: 130, height: 20 },
  { x: 350, y: levelHeight - 900, width: 150, height: 20 },
  { x: 450, y: levelHeight - 1000, width: 120, height: 20 },
  { x: 300, y: levelHeight - 1100, width: 150, height: 20 },
  { x: 400, y: levelHeight - 1200, width: 120, height: 20 },
  { x: 350, y: levelHeight - 1300, width: 150, height: 20 },
  { x: 500, y: levelHeight - 1400, width: 140, height: 20 },
  { x: 450, y: levelHeight - 1500, width: 150, height: 20 },
  { x: 400, y: levelHeight - 1600, width: 120, height: 20 },
  { x: 400, y: levelHeight - 1700, width: 130, height: 20 },
  // Top platform at y = levelHeight - 1800 (e.g., 200 if levelHeight is 2000)
  { x: 400, y: levelHeight - 1800, width: 120, height: 20 }
];


// Place the Yeezy Shoe collectible at a high point (winning condition)
// We set it at, say, y = 50 (near top)
const yeezy = {
  x: canvas.width / 2 - 20,
  y: 20,
  width: 40,
  height: 40,
  collected: false
};

// --- COLLISION DETECTION ---
function rectCollide(r1, r2) {
  return !(r1.x > r2.x + r2.width ||
           r1.x + r1.width < r2.x ||
           r1.y > r2.y + r2.height ||
           r1.y + r1.height < r2.y);
}

// --- GAME LOGIC ---
function updatePlayer() {
  // Horizontal movement
  if (keys.left) {
    player.vx = -player.speed;
  } else if (keys.right) {
    player.vx = player.speed;
  } else {
    player.vx = 0;
  }
  
  // Jumping
  if (keys.up && player.onGround) {
    player.vy = player.jumpStrength;
    player.onGround = false;
  }
  
  // Apply gravity
  player.vy += gravity;
  
  // Update position
  player.x += player.vx;
  player.y += player.vy;
  
  // Prevent going off left/right boundaries of the level
  if (player.x < 0) player.x = 0;
  if (player.x + player.width > canvas.width) player.x = canvas.width - player.width;
  
  // Collision with platforms
  player.onGround = false;
  for (let platform of platforms) {
    if (rectCollide(player, platform)) {
      // Check if player is falling
      if (player.vy > 0 && player.y + player.height - player.vy <= platform.y) {
        player.y = platform.y - player.height;
        player.vy = 0;
        player.onGround = true;
      }
    }
  }
  
  // Check if player collects the yeezy shoe (winning condition)
  if (!yeezy.collected && rectCollide(player, yeezy)) {
    yeezy.collected = true;
    gameWon = true;
    endGame();
  }
  
  // Prevent player from falling below the level
  if (player.y > levelHeight) {
    // Reset player to last platform (for simplicity, send to bottom)
    player.y = levelHeight - player.height - 20;
    player.vy = 0;
  }
}

function updateCamera() {
  // Camera follows the player vertically, but never below 0
  cameraY = player.y - canvas.height/2;
  if (cameraY < 0) cameraY = 0;
  // Also, do not show above level top
  if (cameraY > levelHeight - canvas.height) cameraY = levelHeight - canvas.height;
}

// --- DRAWING FUNCTIONS ---
function drawBackground() {
  // Choose background based on cameraY (player's vertical position)
  // For example:
  // Lower part (cameraY > 1200): Germany
  // Mid part (cameraY between 600 and 1200): Musical area
  // Upper part (cameraY < 600): Mansion area
  if (cameraY > 1200) {
    ctx.drawImage(bgGermany, 0, cameraY, canvas.width, canvas.height, 0, 0, canvas.width, canvas.height);
  } else if (cameraY > 600) {
    ctx.drawImage(bgMusical, 0, cameraY, canvas.width, canvas.height, 0, 0, canvas.width, canvas.height);
  } else {
    ctx.drawImage(bgMansion, 0, cameraY, canvas.width, canvas.height, 0, 0, canvas.width, canvas.height);
  }
}

function drawPlatforms() {
  ctx.fillStyle = "#555";
  for (let platform of platforms) {
    ctx.fillRect(platform.x, platform.y, platform.width, platform.height);
  }
}

function drawPlayer() {
  // Draw player image (Kanye)
  ctx.drawImage(playerImg, player.x, player.y, player.width, player.height);
}

function drawYeezy() {
  if (!yeezy.collected) {
    ctx.drawImage(yeezyImg, yeezy.x, yeezy.y, yeezy.width, yeezy.height);
  }
}

function drawHUD() {
  // Score is drawn in the HUD div, so update it here
  scoreDisplay.textContent = "Score: " + score;
}

function draw() {
  // Clear canvas
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  
  // Save context, then translate for camera scrolling
  ctx.save();
  ctx.translate(0, -cameraY);
  
  // Draw background (it covers the entire canvas area relative to cameraY)
  drawBackground();
  
  // Draw platforms, player, collectible yeezy shoe
  drawPlatforms();
  drawPlayer();
  drawYeezy();
  
  ctx.restore();
  
  drawHUD();
  
  // If game won or over, display message
  if (gameWon) {
    showEndMessage("YOU WIN! Yeezy collected!");
  } else if (gameOver) {
    showEndMessage("GAME OVER");
  }
}

function showEndMessage(text) {
  messageDisplay.style.display = "block";
  messageDisplay.textContent = text;
}

// --- GAME LOOP ---
function gameLoop() {
  if (!gameOver && !gameWon) {
    updatePlayer();
    updateCamera();
    draw();
    requestAnimationFrame(gameLoop);
  } else {
    draw();
  }
}

// --- START THE GAME ---
gameLoop();

// --- OPTIONAL: SCORE INCREASE OVER TIME or from collectibles ---
// (For example, you could add score based on height reached)
