package be.rommens.darts.simulator;

import static be.rommens.darts.simulator.model.Dart.FIRST;
import static be.rommens.darts.simulator.model.Dart.SECOND;
import static be.rommens.darts.simulator.model.Dart.THIRD;
import static org.assertj.core.api.Assertions.assertThat;

import be.rommens.darts.simulator.guide.OptimalPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptimalPathTest {

    @Autowired
    private OptimalPath optimalPath;

    @BeforeEach
    void setUp() {
        optimalPath.fill();
        optimalPath.write();
    }

    @ParameterizedTest
    @ValueSource(ints = {501, 499, 498, 497, 496, 495, 494, 493, 492, 491, 490, 487, 481, 480, 478, 476, 475, 474, 473, 472, 471, 470, 469, 468, 467, 466, 465, 464, 463, 462, 461, 460, 459, 458, 457, 456, 455, 454, 453, 452, 451})
    void test_big_trebbles(int startScore) {
        assertThat(optimalPath.decideAim(FIRST, startScore)).isEqualTo(60);
        assertThat(optimalPath.decideAim(SECOND,startScore - 60)).isEqualTo(60);
        assertThat(optimalPath.decideAim(THIRD,startScore- 60 - 60)).isEqualTo(60);
    }

    @Test
    void test_489() {
        assertThat(optimalPath.decideAim(FIRST,489)).isEqualTo(57);
        assertThat(optimalPath.decideAim(SECOND,489-19)).isEqualTo(60);
        assertThat(optimalPath.decideAim(THIRD,489-19-60)).isEqualTo(60);
    }

    @ParameterizedTest
    @ValueSource(ints = {488, 485, 482})
    void test_488(int startScore) {
        assertThat(optimalPath.decideAim(FIRST,startScore)).isEqualTo(54);
        assertThat(optimalPath.decideAim(SECOND,startScore-18)).isEqualTo(60);
        assertThat(optimalPath.decideAim(THIRD,startScore-18-60)).isEqualTo(60);
    }

    @Test
    void test_486() {
        assertThat(optimalPath.decideAim(FIRST,486)).isEqualTo(57);
        assertThat(optimalPath.decideAim(SECOND,486-57)).isEqualTo(57);
        assertThat(optimalPath.decideAim(THIRD,486-57-57)).isEqualTo(60);

        assertThat(optimalPath.decideAim(FIRST, 486)).isEqualTo(57);
        assertThat(optimalPath.decideAim(SECOND,486-19)).isEqualTo(60);
        assertThat(optimalPath.decideAim(THIRD,486-19-60)).isEqualTo(60);
    }

    @ParameterizedTest
    @ValueSource(ints = {483, 479})
    void test_479(int startScore) {
        assertThat(optimalPath.decideAim(FIRST,startScore)).isEqualTo(57);
        assertThat(optimalPath.decideAim(SECOND,startScore-57)).isEqualTo(57);
        assertThat(optimalPath.decideAim(THIRD,startScore-57-57)).isEqualTo(57);

        assertThat(optimalPath.decideAim(FIRST, startScore)).isEqualTo(57);
        assertThat(optimalPath.decideAim(SECOND,startScore-19)).isEqualTo(60);
        assertThat(optimalPath.decideAim(THIRD,startScore-19-60)).isEqualTo(60);
    }

    @Test
    void test_484() {
        assertThat(optimalPath.decideAim(FIRST,484)).isEqualTo(60);
        assertThat(optimalPath.decideAim(SECOND,484-60)).isEqualTo(60);
        assertThat(optimalPath.decideAim(THIRD,484-60-60)).isEqualTo(60);
    }

    @Test
    void test_128() {
        assertThat(optimalPath.decideAim(FIRST,128)).isEqualTo(54);
        assertThat(optimalPath.decideAim(SECOND,128-18)).isEqualTo(60);
        assertThat(optimalPath.decideAim(THIRD,128-18-60)).isEqualTo(50);
    }

    @Test
    void test303() {
        assertThat(optimalPath.decideAim(FIRST,303)).isEqualTo(57);
        assertThat(optimalPath.decideAim(SECOND,303-57)).isEqualTo(57);
        assertThat(optimalPath.decideAim(THIRD,303-57-57)).isEqualTo(57);

        assertThat(optimalPath.decideAim(FIRST,303)).isEqualTo(57);
        assertThat(optimalPath.decideAim(SECOND,303-57)).isEqualTo(57);
        assertThat(optimalPath.decideAim(THIRD,303-57-19)).isEqualTo(60);

        assertThat(optimalPath.decideAim(FIRST,303)).isEqualTo(57);
        assertThat(optimalPath.decideAim(SECOND,303-19)).isEqualTo(60);
        assertThat(optimalPath.decideAim(THIRD,303-19-60)).isEqualTo(60);
    }
}
