# java-filmorate
Template repository for Filmorate project.

Привет, Яков!  **_Спасибо за проверку_**!

Постарался учесть все комментарии) 

Комментарий: 
Optional внедрил в код, как ты и советовал, но проверки через лямбду на: 
1) userOptional.orElseThrow(() -> new EntityNotFoundException(User.class, 
"Пользователь с ID " + userID + " не найден.")); 
2) filmOptional.orElseThrow(() ->new EntityNotFoundException(Film.class, 
"Фильм с ID " + filmID + " не найден."));
решил оставить в классах InMemory. Надеюсь, это не проблема) 