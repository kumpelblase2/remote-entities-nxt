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
	}

	#TODO
}