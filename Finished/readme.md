# Mario platformer žaidimas
## Paskirtis
Mario žaidimas, kurio tikslas yra nubėgti iki lygio pabaigos, išvengiant priešus ir spastus.
## Paleidimas
Paleidžiama per Intellij IDEA aplinką.
## Funkcionalumas
- Menių sistema (pradžia, kai mirštama, kai lygis yra pabaigiamas).
- Vienas priešo tipas.
- Susidūrimo sistema (gali atpažinti iš kurios pusės įvyko susidūrimas).
- Nukritus į spąstųs žaidėjas gali mirti.
- Game over ekranas

## Pagrindinės klasės
- `Game` - Pagrindinė main klasė, kurioje sukuriama žaidimo gija ir paleidžiamas žaidimo ciklas.
- `Entity` - klasė, iš kurios kyla visi 'veikėjai' - žaidėjas ir priešai.
- `Player` - Klasė, atsakinga už pagrindinį veikėją (jo judesį, animacijas t.t.).
- `LevelManager` - klasė, atsakinga už lygių nuskaitymą ir konvertavimą į žaidžiamą aplinką.
- `LoadSave` - klasė atsakinga už vaizdų importavimą.
- `Constants` - klasė, kuri laiko konstantas, kurios naudojamos visame projekte.

## Plėtimo galimybės
- Level editor.
- Daugiau priešų tipų.
- Laiko skaičiavimo sistema (pvz. kiek laiko žaidėjas užtruko iki lygio pabaigos).
- Daugiau lygių.
- Garso efektai.
- Efektai sudaužant block'us.
- Grybo pridėjimas į žaidimą.

## Projekatvimo Šablonai
- `EnemyManager` - ECS (Entity-Component-System) šablonas naudojamas žaidimo kūrimui.
- `LevelManager` - iteratoriaus šablonas.