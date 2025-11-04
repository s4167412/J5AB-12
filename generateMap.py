map_name = "testRealm"
input = """
+------------------------------------------------+--------------+---------------------------------------------------------+
|            _____ _____ ____ _____              |              |     *          ____        __  __  __     __      *     |
|           |_   _| ____/ ___|_   _|             | item name << |    /          / __ )____ _/ /_/ /_/ /__  / /       \\    |
|             | | |  _| \\___ \\ | |               | item name    |  *|          / __  / __ `/ __/ __/ / _ \\/ /         |*  |
|             | | | |___ ___) || |               | item name    |  ||         / /_/ / /_/ / /_/ /_/ /  __/_/          ||  |
|             |_| |_____|____/_|_|  _            |              |  ||        /_____/\\__,_/\\__/\\__/_/\\___(_)           ||  |
|              / \\  |  _ \\| ____|  / \\           | item name    |  \\\\======_____________________________________======//  |
|             / _ \\ | |_) |  _|   / _ \\          | item name    |            (CharacterName) vs lvl xx (enemy)            |
|            / ___ \\|  _ <| |___ / ___ \\         | item name    |               Difficulty [x] [x] [x] [x]                |
|           /_/   \\_\\_| \\_\\_____/_/   \\_\\        |              |                                                         |
+------------------------------------------------+--------------+            Your speed - 1894ms                          |
| <playername>     || XX/YY HP    || COINS: ZZZZ |      ^^      |                         1682ms - Enemy Speed            |
|    ----------------------------------------    |              |                                                         |
|                                                | <          > |                        You Lost                         |
|            LVL: CCC || XX/YY HP || <enemyname> | <          > |                                                         |
|    ----------------------------------------    |              | Words to type:                                          |
| Tip: (random tip)                              |      vv      |   jfhsajfhsa shfsaf asfshf gpoinsa (Bolded words)       |
+------------------------------------------------+--------------+---------------------------------------------------------+
|                   North (w)                    |              |                                                          
|                                                |              |                                                          
|    West (a)     Inventory (e)     East (d)     |              |                                                          
|                                                |              |                                                          
|                   South (s)                    |              |                                                          
|  Interact (q)                  Save & Exit (0) |              |                                                                          
"""

lines = input.splitlines()[1:]
print(f"ArrayList<StringBuilder> {map_name} = new ArrayList<>(Arrays.asList(")
for i, line in enumerate(lines):
    escaped = line.encode('unicode_escape').decode().replace('"', '\\"')
    if i == len(lines) - 1:
        print(f'    new StringBuilder("{escaped}")')
    else:
        print(f'    new StringBuilder("{escaped}"),')
print("));")