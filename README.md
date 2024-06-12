Matchabl. 
Το UI της εφαρμογής κρατήσεων η οποία δημιουργήθηκε στα πλαίσια του μαθήματος 
Ανάπτυξη Εφαρμογών Πληροφοριακών Συστημάτων. 
Μέσα περιέχει όλα τα activities και τα fragments που παρουσιάστηκαν καθώς και αυτά που είχαν υλοποιηθεί 
μέχει και την ημέρα της εξέτασης αλλά για λόγους λειτουργικότητας δεν παρουσιάστηκαν. 
Δεν προστέθηκε τίποτα παραπάνω. 
Η επικοινωνία με τον υλοποιημένο server μας γίνεται με την τάξη NetworkHandler, η οποία χρησιμοποιεί την
βιβλιοθήκη OkHttp3 για την επικοινωνία με τον server και το handling των requests και για κάθε request
υπάρχει μία κατάλληλη μέθοδος. 
Η οθόνη φορτώματος είναι η SplashScreen και από κει πάμε στην LSActivity για το login ή signup (η οποία
αν ο χρήστης έχει χρησιμοποιήσει την εφαρμογή και έχει λάβει το token, παραλείπεται και μεταφέρεται 
στην MainActivity απευθείας)
Η σειρά των SignUp activities είναι η εξής: FirstSignUpActivity, SignupActivity, SecondSignupActivity, ThirdSignupActivity
Στη Main, όταν επιλεχθεί το search button, ανοίγει το SearchDialogFragment
Όταν ο χρήστης βρίσκεται στο profile του γηπέδου κια θελήσει να κάνει booking, ανοίγει το SportSelectionDialogFragment
