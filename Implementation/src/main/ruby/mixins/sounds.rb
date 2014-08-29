require '../namespace'
require '../sounds/sound_data'

module RemoteEntities
	module EntityMixins
		module EntitySounds
			class << self
				def setup_sounds
					my_sounds = SOUNDS[self.type.name.to_sym]
					my_sounds.each { |type, value|
						self.set_sound type, value
					}
				end
			end
		end
	end
end