package net.superkat.explosiveenhancement.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class SmokeParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
//    private final double startX;
//    private final double startY;
//    private final double startZ;

    SmokeParticle(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.velocityMultiplier = 0.6F;
        this.spriteProvider = spriteProvider;
        this.maxAge = this.random.nextInt(35);
        if(velZ == 0) {
            //for the particles going straight up
            scale = (float) velX * 0.25f;
            this.maxAge += velX * this.random.nextBetween(3, 22);
            this.velocityX = 0;
            this.velocityZ = 0;
        } else if(velX == 0.15 || velX == -0.15) {
            //for the particles where velZ is used as the power variable
            scale = (float) velZ * 0.25f;
            this.maxAge += velZ * this.random.nextBetween(3, 22);
            this.velocityX = velX * (velZ * 0.5);
            this.velocityZ = 0;
        } else if(velZ == 0.15 || velZ == -0.15) {
            //for the particles where velX is used for the power variable
            scale = (float) velX * 0.25f;
            this.maxAge += velX * this.random.nextBetween(3, 22);
            this.velocityX = 0;
            this.velocityZ = velZ * (velX * 0.5);
        }
        this.velocityY = velY / 1.85;
        this.gravityStrength = 3.0E-6F;
        this.collidesWithWorld = true;
        this.setSpriteForAge(spriteProvider);
    }

    public void tick() {
//        int direction = this.random.nextBetween(1, 4);
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
            if (this.age == 12) {
                this.velocityX = 0;
                this.velocityY = 0.05;
                this.velocityZ = 0;
            }
            this.move(this.velocityX, this.velocityY, this.velocityZ);
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new SmokeParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}