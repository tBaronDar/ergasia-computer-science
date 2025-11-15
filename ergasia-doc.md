# Εργασια για το μαθημα Θέματα επιστημης υπολογιστών

Ενα μικρο προγραμμα που εκτελει,σταματάει και απεικονίζει κάποια processes του λειτουργικού συτηματος. Το processtool είναι γραμμένο σε **Java(JDK 25 LTS)** και το project δημιουργηθηκε με το Maven για να μπορουμε να κανουμε package σε εκτελέσιμο .jar. Χρησιμοποιει το swing για την δημιουργία γραφικού περιβαλοντος. Εχει αναπτυχθεί σε περιβαλλον Linux(Ubuntu 24.04), δεν εχει τεσταριστει σε περιβαλλον Windows. Δεν ξερω αν η χρηση Windows δημιουργει καποιο προβλημα στην απεικόνισει των διαδικασιων.

Η ιδέα για αυτο το εργαλειο μου ηρθε απο την εργασια capstone του περασμενου εξαμηνου. Με την ομαδα μου θελαμε να τρεξουμε σκριπτακια python -τα οποια ειχαν να κανουν με την ανάλυση δεδομένων, μεσα απο το Java application. Οπότε επρεπε να αναζητησω πως γινεται αυτο. Μπορειτε να δειτε την υλοποιηση του **ProcessBuilder** στο περσινό capstone [εδω](https://github.com/Omada-K/retailHUB/blob/main/src/main/java/com/service/PythonRunner.java).

## Περιγραφη

Στην κλασση [ProcessManager](https://github.com/tBaronDar/ergasia-computer-science/blob/main/src/main/java/com/themisdarelis/processtool/service/ProcessManager.java), το εργαλειο που κανει το μεγαλυτερο μέρος της δουλειας, ειναι το [ProcessBuilder](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html). Το οποιο ξεκιναει και σταματει τα processes στην κονσολα(shell) του λειτουργικου. Επειτα με το **HandleProcess** δημιουργω μια λιστα τυπου [ProcessInfo](https://github.com/tBaronDar/ergasia-computer-science/blob/main/src/main/java/com/themisdarelis/processtool/model/ProcessInfo.java).

Η λιστα αυτη χρεισημοποιειτε απο το [ProcessInfoTableModel](https://github.com/tBaronDar/ergasia-computer-science/blob/main/src/main/java/com/themisdarelis/processtool/ui/ProcessInfoTableModel.java) για την δημιουργια ενος μοντελου πινακα και τελος το μοντελο αυτο απεικονιζεται με ως **JTable** στο [MainPanel](https://github.com/tBaronDar/ergasia-computer-science/blob/main/src/main/java/com/themisdarelis/processtool/ui/MainPanel.java).

Στο MainPanel προσθετονται και αλλα _"J"_ στοιχεια(κουμπια, labels, input κτλ). Το πανελ αυτο ειναι το μοναδικο πανελ της εφαρμαγής και ειναι η 'εισοδος' τους κεντρικου **JFrame**(δηλαδη παραθυρου) με ονομα [MainFrame](https://github.com/tBaronDar/ergasia-computer-science/blob/main/src/main/java/com/themisdarelis/processtool/ui/MainFrame.java).

Οταν πατήσετε το κουμπί **Start Process** θα γινουν τα παρακάτω βήματα:

1. Η μέθοδο **ProcessManager.startProcess()** θα εκετελεστει με παραμετρο το String του input field.
2. Θα καθαριστεί το κείμενο μεσα στο textInput
3. Μια καινουργια λιστα διεργασιών θα δημιουργηθει
4. Θα ανανεωθεί ο πινακας με την καινοθργια λιστα
5. Η βοηθητική **scrollToBottom()** θα καλεστει μεσω του **SwingUtilities.invokeLater()**, για να γινει σκρολ στο τέλος του πινακα
6. Ενα μηνυμα **JOptionPane.showMessageDialog()** θα δημιουργηθει.
7. Θα επιλεχθει η τελευταια γραμμη του πινακα. Εκει βρισκεται η διεργασια που μολις ξεκινησαμε.

Οταν πατήσετε το κουμπί **Kill Process** θα γινουν τα παρακάτω βήματα:

1. Θα παρουμε την επιλεγμένη σειρα(row) μεσω του processTable.getSelectedRow()
2. Απο το αντικείμενο **selectedRow** Θα εξαγουμε το id της διεργασιας(pid)
3. Θα τρεξει η μεθοδος **ProcessManager.killProcess()** με ορισμα το pid
4. Μια καινουργια λιστα διεργασιών θα δημιουργηθει
5. Θα ανανεωθεί ο πινακας με την καινουργια λιστα
6. Η βοηθητική **scrollToBottom()** θα καλεστει μεσω του **SwingUtilities.invokeLater()**, για να γινει σκρολ στο τέλος του πινακα
7. Ενα μηνυμα **JOptionPane.showMessageDialog()** θα δημιουργηθει.

## Τι λειπει

- Προγραματα που οταν ξεκινουν ανοιγουν πολλεσ διεργασιες μαζι δεν σταματατουν αν επιλεξουμε και κλεισουμε μονο μια απο αυτες.
- Αν ανοιγουμε/κλεινουμε προγραμματα εκτος του processtool (κανονικά από το 'Χ'). Η λιστα των διεργασιων δεν ανανεώνεται αυτοματα. Γι'αυτο εβαλα και το **Refresh** κουμπι.
- Για να εχω το κεφαλι μου ησυχο, απο την λιστα των διεργασιες εξερουνται εκουσια οι διεργασιες που ανοικουν στον root.
- Για καποιον λογο που δεν ξερω το scrolling δεν λειτουργει οταν η εφαρμογη ξεκιναει, αλλα λειτουργει κανονικα οταν ξεκιναω μια καινουργια διεργασια...

---
