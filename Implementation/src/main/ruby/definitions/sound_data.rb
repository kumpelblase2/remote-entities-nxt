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
	}

	#TODO
}