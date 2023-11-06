package lotto;

import camp.nextstep.edu.missionutils.Randoms;
import lotto.domain.Lotto;
import lotto.domain.Prize;
import lotto.domain.Result;

import java.util.*;
import java.util.stream.Collectors;

public class LottoGame {

    List<Lotto> lottos;
    Lotto winningNumber;
    Integer bonusNumber;
    List<Prize> prizeRanks;
    Result result;

    public LottoGame() {
        this.lottos = new ArrayList<>();
        this.prizeRanks = new ArrayList<>();
        this.result = new Result();
    }


    public void buy(int quantity) {
        for (int q = 0; q < quantity; q++) {
            List<Integer> numbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);

            Lotto lotto = new Lotto(numbers);
            lottos.add(lotto);
        }
    }

    public List<List<Integer>> getLottos() {
        List<List<Integer>> lottosNumbers = new ArrayList<>();
        for (Lotto lotto : lottos) {
            List<Integer> numbers = lotto.getNumbers();
            lottosNumbers.add(numbers);
        }
        return lottosNumbers;
    }

    public void setWinningNumber(String[] numbers) {
        List<Integer> winningNumber = Arrays.stream(numbers)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        this.winningNumber = new Lotto(winningNumber);
    }

    public void setBonusNumber(String number) {
        this.bonusNumber = Integer.parseInt(number);
    }

    public void confirmWin() {
        collectPrizeRanks();
    }

    private void collectPrizeRanks() {
        for (Lotto lotto : lottos) {
            long correct = lotto.getNumbers().stream()
                    .filter(number -> winningNumber.getNumbers().contains(number))
                    .count();
            prizeRanks.add(getRank(correct, lotto));
        }
    }

    private Boolean isSecondPrize(Lotto lotto) {
        return lotto.getNumbers().contains(bonusNumber);
    }

    private Prize getRank(long correct, Lotto lotto) {
        Prize prize = Arrays.stream(Prize.values())
                .filter(p ->
                        p.getMatched() == correct
                )
                .findAny()
                .orElse(Prize.EMPTY);
        if (correct == 5 && isSecondPrize(lotto)) {
            prize = Prize.SECOND;
        }
        return prize;
    }

    public void setResult() {
        result.setResult(prizeRanks);
    }

    public int[] getResult() {
        return result.getResult();
    }

    public long getTotalEarnings() {
        long total = 0;
        for (Prize prize : prizeRanks) {
            total = prize.calculate(total);
        }
        return total;
    }
}
