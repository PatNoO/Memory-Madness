<img width="412" height="917" alt="Difficulty" src="https://github.com/user-attachments/assets/a77d93e6-8480-4bb6-9cfd-56e72a059030" />
<img width="412" height="917" alt="StartMenu" src="https://github.com/user-attachments/assets/83c5ba8b-ed87-4ec4-bf97-c398f724bee6" />
<img width="412" height="917" alt="Game" src="https://github.com/user-attachments/assets/8f56661a-b19c-4475-8f1c-8005ad7e5c2b" />
<img width="412" height="917" alt="Win-2" src="https://github.com/user-attachments/assets/d460b062-97c9-4a34-a040-eb2869aa5d01" />

# 🎮 Memory Madness

A fast-paced and customizable Android memory game built with Kotlin, Fragments, ViewModels, and SharedPreferences.
Choose your difficulty, pick a theme, enable optional pause help, and try to beat your best score!

# 📱 Features

⭐ Game Modes

Memory Madness includes three difficulty levels:

**Easy – 6 cards (3 pairs)**

**Medium – 12 cards (6 pairs)**

**Hard – 18 cards (9 pairs)**

Each mode uses the same logic, only the number of cards changes.

## 🎨 Multiple Card Themes

Players can choose between four different themes:

**Halloween**

**Christmas**

**Easter**

**St. Patrick’s Day**

Themes change all card images dynamically.

⏱️ Timer, Moves & Pause

Built-in countdown timer

Move counter

Optional pause switch (can be turned on/off)

## 🏆 High Score System

Memory Madness saves your results (name, difficulty, time, moves, and theme) using SharedPreferences.
High scores are shown in a dedicated screen with filters for:

**Easy**

**Medium**

**Hard**

## 🧩 Gameplay Highlights

Smooth card-flip logic

Pair matching system

Built using CardManager objects stored as tags on ImageViews

Prevents double-click spam using internal “busy state”

Win screen with statistics & score saving

Play again or return to main menu

## 🛠️ Tech Stack

Kotlin

Android Fragments

ViewModel + LiveData

View Binding

Coroutines (Timers & UI updates)

SharedPreferences + Gson (for persistent scoring)

RecyclerView (High score list)

## 🗂️ Project Structure (Overview)

**📂activitys**  
    MainActivity  
    StartActivity  

**📂fragments**  
    HomeMenuFragment  
    DifficultyFragment  
    HighScoreFragment  
    WinFragment  
    **📂game_play**  
        EasyFragment  
        MediumFragment  
        HardFragment  

**📂view_model**  
    PlayerViewModel  
    GameViewModel  

**📂data_class**  
    Player  
    CardManager  

**📂utility**  
    loadPrefsScore  
    savedPrefsScore  

**📂adapter**  
    HighScoreRecyclerAdapter  

**📂enum_class**  
    CardTheme

## 🚀 How to Play

Start the app and enter your player name.

Choose your difficulty.

Pick a card theme.

(Optional) Enable Pause Help.

Start matching cards before time runs out!

Save your score and try to climb the leaderboard.

📸 Screenshots

<img width="396" height="879" alt="memory_madness_start_screen" src="https://github.com/user-attachments/assets/98eb561a-7ed2-4497-84e2-3a95c2dc6bc9" />

<img width="396" height="879" alt="memory_madness_home_menu" src="https://github.com/user-attachments/assets/3935e848-b571-4b63-837c-f2224f4175b9" />

<img width="396" height="879" alt="memory_madness_medium_play" src="https://github.com/user-attachments/assets/93ddd115-0185-4c37-961c-f53d6d32d376" />

<img width="396" height="879" alt="memory_madness_hard_play" src="https://github.com/user-attachments/assets/939adcef-0f30-4bb7-ae5f-c2a1eeab0044" />

<img width="396" height="879" alt="memory_madness_high_score" src="https://github.com/user-attachments/assets/e62f05c1-1b14-4efa-be8d-e41d82056b4e" />

<img width="396" height="879" alt="memory_madness_win_scren" src="https://github.com/user-attachments/assets/b72db24f-4556-4c04-a2fa-972c6ab6dfcf" />


## 💡 Future Improvements

Sound effects & animations

More themes

Multiplayer mode


## 📚 Sources & References

**YouTube video used as learning resources during development:**
- Link 1: [*[Building Android Matching Game]* — (https://...)](https://www.youtube.com/watch?v=vsACLOU-DgE&t=1331s)
- Link 2: [[*[Making a Simple Android Memory Game]* — (https://...)](https://www.youtube.com/watch?v=vsACLOU-DgE&t=1331s)](https://www.youtube.com/watch?v=B7--mnjOOI4&t=621s)

**Additional resources:**
- https://kt.academy/article/fk-cp-sorting
- AI assistance for debugging, and explanations  
- Android Developer Documentation  
- Kotlin Documentation   

## 🧑‍💻 Author

Memory Madness created by [Patrik Noordh]
