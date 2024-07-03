package main;

// Hier war ein überflüssiges Import-Statement
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Plattform
{
    private int x, y;
    private int plattformLaenge, plattformHoehe;

    // schaedlich gibt an, ob eine Plattform Schaden macht und wenn ja, wie viel
    private int schaedlich = 0;

    // Die Bilder der einzelnen Segmente werden in diesem Array gespeichert
    private BufferedImage[] bild;

    // Diese Arraylist wird benötigt, um die Grafik der Plattform in der Klasse main.World einfach darstellen zu können; in ihr werden x- und y-Koordinaten der einzelnen Plattformteile gespeichert
    ArrayList<int[]> positions = new ArrayList<>();
    protected boolean alreadyInitialized = false;
    private int platformImageSize;

    public Plattform()
    {
        try
        {
            platformImageSize = ImageIO.read(new File("res/Brick_eckeOL1.png")).getHeight()*3;
        }
        catch(Exception e)
        {
            System.out.println("Keine Ecke gefunden für die Größe der Bilder");
        }
    }


    // Einheit für plattformlaenge und hoehe in Blöcken
    public void erzeugePlattform(int x, int y, int plattformLaenge, int plattformHoehe, int schaedlich) throws IOException, NullPointerException
    {
        // Wurde die Plattform bereits erstellt? Dann nicht nochmal erstellen!
        if(alreadyInitialized)
        {
            System.out.println("Was zum Fick, die Plattform gibts ja schon");
            return;
        }

        this.x = x;
        this.y = y;
        this.plattformLaenge = plattformLaenge;
        this.plattformHoehe = plattformHoehe;
        this.schaedlich = schaedlich;

        int zeiger = 4;

        bild = new BufferedImage[plattformHoehe*plattformLaenge];

        // Hier wird die Größe der einzelnen Blöcke festgelegt. *3, sonst zu klein; kann aber noch angepasst werden
        int platformImageSize = ImageIO.read(new File("res/Brick_eckeOL1.png")).getHeight()*3;

        // Erstellen der Ecken
        bild[0] = ImageIO.read(new File("res/Brick_kanteOL1.png"));
        positions.add(0, new int[]{x, y});
        bild[1] = ImageIO.read(new File("res/Brick_kanteOR1.png"));
        positions.add(1, new int[]{(plattformLaenge-1)*platformImageSize+x, y});
        bild[2] = ImageIO.read(new File("res/Brick_kanteUL1.png"));
        positions.add(2, new int[]{x, (plattformHoehe-1)*platformImageSize+y});
        bild[3] = ImageIO.read(new File("res/Brick_kanteUR1.png"));
        positions.add(3, new int[]{(plattformLaenge-1)*platformImageSize+x, (plattformHoehe-1)*platformImageSize+y});

        // Wie viele Plattformen müssen jeweils oben und unten erstellt werden?
        int zuErzeugendePlattformenObenUndUnten = plattformLaenge - 2;

        // Erstellen der oberen und unteren Segmente
        for(int i=0; i<zuErzeugendePlattformenObenUndUnten; i++)
        {
            // Einfügen der Plattformbilder oben an der Plattform und Errechnen der Position
            bild[zeiger] = ImageIO.read(new File("res/Brick_oben1.png"));
            positions.add(zeiger, new int[]{(i+1)*platformImageSize+x, y});
            zeiger++;
            // Einfügen der Plattformbilder unten an der Plattform und Errechnen der Position
            bild[zeiger] = ImageIO.read(new File("res/Brick_unten1.png"));
            positions.add(zeiger, new int[]{(i+1)*platformImageSize+x, y+(plattformHoehe-1)*platformImageSize});
            zeiger++;
        }

        // Wie viele Plattformen müssen jeweils links und rechts erstellt werden?
        int zuErzeugendePlattformenLinksUndRechts = plattformHoehe - 2;

        // Erstellen der linken und rechten Segmente
        for(int i=0; i<zuErzeugendePlattformenLinksUndRechts; i++)
        {
            // Einfügen der Plattformbilder links an der Plattform und Errechnen der Position
            bild[zeiger] = ImageIO.read(new File("res/Brick_links1.png"));
            positions.add(zeiger, new int[]{x, (i+1)*platformImageSize+y});
            zeiger++;
            // Einfügen der Plattformbilder rechts an der Plattform und Errechnen der Position
            bild[zeiger] = ImageIO.read(new File("res/Brick_rechts1.png"));
            positions.add(zeiger, new int[]{x+(plattformLaenge-1)*platformImageSize, (i+1)*platformImageSize+y});
            zeiger++;
        }

        // Wie viele Mittelteile müssen erstellt werden?
        int zuErzeugendeMittelStuecke = bild.length - 2*plattformHoehe - 2*(plattformLaenge-2);

        int currentRow = 1;
        int piecesPlacedOnCurrentRow = 0;

        // Erstellen der Mittelstücke
        for(int i=0;i<zuErzeugendeMittelStuecke;i++)
        {
            // Einfügen der Mittelstück-Bilder ins Array
            bild[zeiger] = ImageIO.read(new File("res/Brick_mitte1.png"));

            // Ist die aktuelle Reihe schon voll? Wenn ja, gehe zur nächsten und fange mit dem Platzieren wieder "von vorne" an.
            if(piecesPlacedOnCurrentRow>=zuErzeugendePlattformenObenUndUnten)
            {
                currentRow++;
                piecesPlacedOnCurrentRow = 0;
            }

            // Hier wird die Position des Teils in Abhängigkeit von der aktuellen Reihe und der Anzahl an schon in dieser Reihe platzierten Plattformen berechnet.
            positions.add(zeiger, new int[]{(piecesPlacedOnCurrentRow+1)*platformImageSize+x, currentRow*platformImageSize+y});

            piecesPlacedOnCurrentRow++;
            zeiger++;
        }

        // Die Plattform wurde jetzt initialisiert.
        alreadyInitialized = true;
    }

    public BufferedImage[] getImages() {

        return bild;

    }
    public int getSchaedlich() {

        return schaedlich;

    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int[] getDimensions()
    {
        return new int[]{plattformLaenge*platformImageSize,plattformHoehe*platformImageSize};
    }


    public ArrayList<int[]> getPositions()
    {
        return positions;
    }
}