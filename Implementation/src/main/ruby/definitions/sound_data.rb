require 'java'
require '../base'

SOUNDS = {
    :Cow => {
        RemoteEntities::EntitySound::RANDOM => 'mob.cow.say',
        RemoteEntities::EntitySound::HURT => 'mob.cow.hurt',
        RemoteEntities::EntitySound::DEATH => 'mob.cow.hurt',
        RemoteEntities::EntitySound::STEP => 'mob.cow.step',
    },

	:Bat => {
		RemoteEntities::EntitySound::SLEEPING => 'mob.bat.idle',
		RemoteEntities::EntitySound::HURT => 'mob.bat.hurt',
		RemoteEntities::EntitySound::DEATH => 'mob.bat.death'
	},

	:Zombie => {
		RemoteEntities::EntitySound::RANDOM => 'mob.zombie.say',
		RemoteEntities::EntitySound::HURT => 'mob.zombie.hurt',
		RemoteEntities::EntitySound::DEATH => 'mob.zombie.death',
		RemoteEntities::EntitySound::STEP => 'mob.zombie.step'
	},

	:Wolf => {
		RemoteEntities::EntitySound::RANDOM => {
			:growl => 'mob.wolf.growl',
			:whine => 'mob.wolf.whine',
			:panting => 'mob.wolf.panting',
			:bark => 'mob.wolf.bark'
		},
		RemoteEntities::EntitySound::HURT => 'mob.wolf.hurt',
		RemoteEntities::EntitySound::DEATH => 'mob.wolf.death',
		RemoteEntities::EntitySound::STEP => 'mob.wolf.step'
	},

	:Wither => {
		RemoteEntities::EntitySound::RANDOM => 'mob.wither.idle',
		RemoteEntities::EntitySound::HURT => 'mob.wither.hurt',
		RemoteEntities::EntitySound::DEATH => 'mob.wither.death'
	},

	:Witch => {
		RemoteEntities::EntitySound::RANDOM => 'mob.witch.idle',
		RemoteEntities::EntitySound::HURT => 'mob.witch.hurt',
		RemoteEntities::EntitySound::DEATH => 'mob.witch.death'
	},

	:Villager => {
		RemoteEntities::EntitySound::RANDOM => {
				:haggle => 'mob.villager.haggle',
				:idle => 'mob.wolf.idle'
		},
		RemoteEntities::EntitySound::HURT => 'mob.villager.hit',
		RemoteEntities::EntitySound::DEATH => 'mob.villager.death',
		RemoteEntities::EntitySound::STEP => 'mob.villager.step',
		RemoteEntities::EntitySound::YES => 'mob.villager.yes',
		RemoteEntities::EntitySound::NO => 'mob.villager.no'
	},

	:Spider => {
		RemoteEntities::EntitySound::RANDOM => 'mob.spider.say',
		RemoteEntities::EntitySound::HURT => 'mob.spider.say',
		RemoteEntities::EntitySound::DEATH => 'mob.spider.death',
		RemoteEntities::EntitySound::STEP => 'mob.spider.step'
	},

	:Snowman => {
		RemoteEntities::EntitySound::RANDOM => 'none',
		RemoteEntities::EntitySound::HURT => 'none',
		RemoteEntities::EntitySound::DEATH => 'none',
		RemoteEntities::EntitySound::ATTACK => 'random.bow'
	},

	:Slime => {
		RemoteEntities::EntitySound::HURT => {
				:big => 'mob.slime.big',
				:small => 'mob.slime.smalle'
		},
		RemoteEntities::EntitySound::DEATH => {
				:big => 'mob.slime.big',
				:small => 'mob.slime.smalle'
		},
		RemoteEntities::EntitySound::STEP => {
				:big => 'mob.slime.big',
				:small => 'mob.slime.smalle'
		},
		RemoteEntities::EntitySound::ATTACK => 'mob.attack'
	},

	:Skeleton => {
		RemoteEntities::EntitySound::RANDOM => 'mob.skeleton.say',
		RemoteEntities::EntitySound::HURT => 'mob.skeleton.hurt',
		RemoteEntities::EntitySound::DEATH => 'mob.skeleton.death',
		RemoteEntities::EntitySound::STEP => 'mob.skeleton.step',
		RemoteEntities::EntitySound::ATTACK => 'random.bow'
	},

	:Silverfish => {
		RemoteEntities::EntitySound::RANDOM => 'mob.silverfish.say',
		RemoteEntities::EntitySound::HURT => 'mob.silverfish.hit',
		RemoteEntities::EntitySound::DEATH => 'mob.silverfish.kill',
		RemoteEntities::EntitySound::STEP => 'mob.silverfish.step'
	},

	:Sheep => {
		RemoteEntities::EntitySound::RANDOM => 'mob.sheep.say',
		RemoteEntities::EntitySound::HURT => 'mob.sheep.say',
		RemoteEntities::EntitySound::DEATH => 'mob.sheep.say',
		RemoteEntities::EntitySound::STEP => 'mob.sheep.step'
	},

	:Pigmen => {
		RemoteEntities::EntitySound::RANDOM => 'mob.zombiepig.zpig',
		RemoteEntities::EntitySound::HURT => 'mob.zombiepig.zpighurt',
		RemoteEntities::EntitySound::DEATH => 'mob.zombiepig.zpigdeath'
	},

	:Pig => {
		RemoteEntities::EntitySound::RANDOM => 'mob.pig.say',
		RemoteEntities::EntitySound::HURT => 'mob.pig.say',
		RemoteEntities::EntitySound::DEATH => 'mob.pig.death',
		RemoteEntities::EntitySound::STEP => 'mob.pig.step'
	},

	:Ocelote => {
		RemoteEntities::EntitySound::RANDOM => {
			:purr => 'mob.cat.purr',
			:purreow => 'mob.cat.purreow',
			:meow => 'mob.cat.meow'
		},
		RemoteEntities::EntitySound::HURT => 'mob.cat.hitt',
		RemoteEntities::EntitySound::DEATH => 'mob.cat.hitt'
	},

	:Mushroom => {
		RemoteEntities::EntitySound::RANDOM => 'mob.cow.say',
		RemoteEntities::EntitySound::HURT => 'mob.cow.hurt',
		RemoteEntities::EntitySound::DEATH => 'mob.cow.hurt',
		RemoteEntities::EntitySound::STEP => 'mob.cow.step'
	},

	:LavaSlime => {
		RemoteEntities::EntitySound::HURT => {
			:big => 'mob.slime.big',
			:small => 'mob.slime.small'
		},
		RemoteEntities::EntitySound::DEATH => {
			:big => 'mob.slime.big',
			:small => 'mob.slime.small'
		},
		RemoteEntities::EntitySound::STEP => {
			:big => 'mob.slime.big',
			:small => 'mob.slime.small'
		},
		RemoteEntities::EntitySound::ATTACK => 'mob.attack'
	},

	:IronGolem => {
		RemoteEntities::EntitySound::RANDOM => 'none',
		RemoteEntities::EntitySound::HURT => 'mob.irongolem.hit',
		RemoteEntities::EntitySound::DEATH => 'mob.irongolem.death',
		RemoteEntities::EntitySound::STEP => 'mob.irongolem.walk'
	},

	:Horse => {
		RemoteEntities::EntitySound::LAND => 'mob.horse.land'
	},

	:Ghast => {
		RemoteEntities::EntitySound::RANDOM => 'mob.ghast.moan',
		RemoteEntities::EntitySound::HURT => 'mob.ghast.scream',
		RemoteEntities::EntitySound::DEATH => 'mob.ghast.death'
	},

	:Enderman => {
		RemoteEntities::EntitySound::RANDOM => {
			:idle => 'mod.enderman.idle',
			:scream => 'mob.endermen.scream'
		},
		RemoteEntities::EntitySound::HURT => 'mob.endermen.hit',
		RemoteEntities::EntitySound::DEATH => 'mob.endermen.death',
		RemoteEntities::EntitySound::TELEPORT => 'mob.endermen.portal'
	},

	:Enderdragon => {
		RemoteEntities::EntitySound::RANDOM => 'mob.enderdragon.growl',
		RemoteEntities::EntitySound::HURT => 'mob.enderdragon.hit'
	},

	:Creeper => {
		RemoteEntities::EntitySound::RANDOM => 'mob.creeper.say',
		RemoteEntities::EntitySound::DEATH => 'mob.creeper.death'
	},

	:Blaze => {
		RemoteEntities::EntitySound::RANDOM => 'mob.blaze.breathe',
		RemoteEntities::EntitySound::DEATH => 'mob.blaze.death',
		RemoteEntities::EntitySound::HURT => 'mob.blaze.hit'
	},

	:CaveSpider => {
		RemoteEntities::EntitySound::RANDOM => 'mob.spider.say',
		RemoteEntities::EntitySound::HURT => 'mob.spider.say',
		RemoteEntities::EntitySound::DEATH => 'mob.spider.death',
		RemoteEntities::EntitySound::STEP => 'mob.spider.step'
	},

	:Chicken => {
		RemoteEntities::EntitySound::RANDOM => 'mob.chicken.say',
		RemoteEntities::EntitySound::HURT => 'mob.chicken.hurt',
		RemoteEntities::EntitySound::DEATH => 'mob.chicken.hurt',
		RemoteEntities::EntitySound::STEP => 'mob.chicken.step'
	}
}