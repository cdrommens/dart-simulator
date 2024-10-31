package be.rommens.darts.simulator;

import static java.lang.Math.toDegrees;

import be.rommens.darts.simulator.model.Leg;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Statistics;
import be.rommens.darts.simulator.model.Turn;
import be.rommens.darts.simulator.strategy.polar.WriteUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.ImageIcon;
import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.chart.ui.Align;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("polar")
@SpringBootTest
public class legSimulatorPolarTest {

    @Autowired
    private LegSimulator legSimulator;

    @Test
    void singleGameTest() throws IOException {
        var leg = legSimulator.playLeg(new Player("Humphries",25,50,95,42,44, 43, 108));
        XYSeries series = new XYSeries("hits");
        for(Turn turn : leg.turns()) {
            System.out.println(turn.getStartScore() + " : " + turn.getScoreThrown() + " (" + turn + ")");
            turn.getThrows().forEach(t -> series.add(Math.toDegrees(t.point().getAzimuth()), t.point().getRadius()));
        }
        leg.statistics().write();
        WriteUtils.draw(series);
    }

    @Test
    void testBatch() {
        List<Statistics> statistics = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            var result = legSimulator.playLeg(new Player("Humphries",25,50,95,42,44, 43, 108));
            statistics.add(result.statistics());
        }
        System.out.println("First 9 avg : "+ statistics.stream().mapToDouble(Statistics::getFirst9Average).average().orElseThrow());
        System.out.println("Avg : " + statistics.stream().mapToDouble(Statistics::getAverage).average().orElseThrow());
        System.out.println("checkout : "+ statistics.stream().mapToDouble(Statistics::getCheckoutPercentage).average().orElseThrow());
    }

    @Test
    void testBatchUntilError() {
        Leg leg;
        do {
            leg = legSimulator.playLeg(new Player("Humphries",25,50,95,42,44, 43, 108));
            leg.statistics().write();
        } while (leg.statistics().getNumberOfDartsThrown() < 22);
    }


    // Test for drawing circle
    @Test
    void testTrebleSpread() throws IOException {
        XYSeries series = new XYSeries("Test");
        int num = 0;
        while (num < 9) {
            var aim = Vector2D.of(103,0);
            var point = Vector2D.of(
                    ThreadLocalRandom.current().nextGaussian(aim.getX(), 15),
                    ThreadLocalRandom.current().nextGaussian(aim.getY(), 15));

            PolarCoordinates result2 = PolarCoordinates.fromCartesian(point);
            PolarCoordinates center = PolarCoordinates.fromCartesian(aim);
            var center2d = center.toCartesian();
            var punt2d = result2.toCartesian();

            var rek = Math.pow((punt2d.getX() - center2d.getX()),2) + Math.pow((punt2d.getY() - center2d.getY()),2);
            //for example, humphries first 9 108.89
            if (rek < (Math.pow((20-10.889), 2))) {
                series.add(toDegrees(result2.getAzimuth()), result2.getRadius());
                num++;
            } else {
                var r = ThreadLocalRandom.current().nextInt(100);
                // for example, humphries treble hit % 43
                if (r < 43) {
                    System.out.println("out en again");
                } else {
                    series.add(toDegrees(result2.getAzimuth()), result2.getRadius());
                    System.out.println("out but counts");
                    num++;
                }
            }
        }
        draw(series);
    }


    private static void draw(XYSeries series) throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(series);

        ValueAxis radiusAxis = new NumberAxis();
        radiusAxis.setUpperBound(226);
        radiusAxis.setTickLabelsVisible(false);
        radiusAxis.setAxisLineVisible(false);
        radiusAxis.setTickMarksVisible(false);
        DefaultPolarItemRenderer renderer = new DefaultPolarItemRenderer();
        renderer.setShapesVisible(true);
        Stroke dashedStroke = new BasicStroke(
                0.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                0.0f, new float[] {0.0f, 1e10f}, 1.0f );
        renderer.setSeriesStroke(0, dashedStroke);

        ImageIcon icon = new ImageIcon(LegSimulator.class.getClassLoader().getResource("img.png"));

        PolarPlot plot = new PolarPlot(dataset, radiusAxis, renderer);
        plot.setCounterClockwise(false);
        plot.setBackgroundImage(icon.getImage());
        plot.setBackgroundImageAlignment(Align.FIT);
        //plot.setBackgroundImageAlpha(1.0f);
        plot.setMargin(0);
        plot.setInsets(RectangleInsets.ZERO_INSETS);
        plot.setBackgroundPaint(new Color(255,255,255,0));

        JFreeChart chart = new JFreeChart(null, plot);
        chart.setBackgroundPaint(new Color(255,255,255,0));
        chart.removeLegend();

        File file = new File("./out.png");
        ChartUtils.saveChartAsPNG(file, chart, 1000,1000);
    }

}
