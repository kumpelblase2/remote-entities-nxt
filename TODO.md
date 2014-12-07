# Things that still need to be done
## Package
I don't think that maven will, at this point, create a proper package that also includes the ruby sources and compile.

## API
The API is pretty much done at this point. By that I mean, theoretically it should not miss anything that was previously needed or present. This does not include new features introduced in newer minecraft versions, which will probably be present through the bukkit API somehow anyway. Otherwise we can just implement it ourselves, easy peasy.

## Implementations
- Every entity should be implemented, at least to a minimum extend. However, their default desires don't exist yet.
- Desires are missing to great extend. Basically every desire except: Swim, AcceptFlower, AvoidSpecific, AvoidEntity, AvoidSun. So basically every desire that is under /Implementation/src/main/java/de/kumpelblase2/remoteentities/api/goals/ needs to be ported to ruby.
- Entity configuration is not updated with new classes. see Implementation/ruby/config/entity_settings.rb
- Mechanism for overriding NMS methods has to be tested throughout.

## Updated to newer MC version
The port is still running MC 1.7.4, thus it not only needs to get the new definitions but also the new behavior changes introduced in later updates (especially the 1.8 update). The biggest part will probably be updating the player entity again, at least I would expect that to be one of the bigger issues like it was with previous updates. Any new entities? I don't think so, but if there are, those are a lower priority than getting the existing ones to work.

## Testing
I want to try and get around 70+% of code coverage with unit tests for both the ruby and java implementation