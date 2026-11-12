package by.bseu.pp.lab04;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class SalesAnalyzer {
    private SalesAnalyzer() { }

    public static List<Sale> filterByPeriod(List<Sale> sales, LocalDate from, LocalDate to) {
        validateSales(sales);
        validatePeriod(from, to);
        return sales.stream()
                .filter(sale -> !sale.getDate().isBefore(from) && !sale.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    public static List<Sale> findByProductCode(List<Sale> sales, String productCode) {
        validateSales(sales);
        if (productCode == null || productCode.isBlank()) {
            throw new IllegalArgumentException("Код товара должен быть непустым");
        }
        List<Sale> result = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getProductCode().equals(productCode)) {
                result.add(sale);
            }
        }
        return result;
    }

    public static List<Sale> sortByAmountDescending(List<Sale> sales) {
        validateSales(sales);
        List<Sale> result = new ArrayList<>(sales);
        result.sort(Comparator.comparingDouble(Sale::getAmount).reversed()
                .thenComparing(Sale::getId));
        return result;
    }

    public static Set<String> uniqueCustomerCodes(List<Sale> sales) {
        validateSales(sales);
        Set<String> result = new HashSet<>();
        for (Sale sale : sales) {
            result.add(sale.getCustomerCode());
        }
        return result;
    }

    public static Map<String, List<Sale>> groupByCategory(List<Sale> sales) {
        validateSales(sales);
        return sales.stream()
                .collect(Collectors.groupingBy(Sale::getCategory));
    }

    public static double totalAmountForPeriod(List<Sale> sales, LocalDate from, LocalDate to) {
        validateSales(sales);
        validatePeriod(from, to);
        return filterByPeriod(sales, from, to).stream()
                .mapToDouble(Sale::getAmount)
                .sum();
    }

    public static Map<String, Double> amountByCategoryForPeriod(List<Sale> sales, LocalDate from, LocalDate to) {
        validateSales(sales);
        validatePeriod(from, to);
        return filterByPeriod(sales, from, to).stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.summingDouble(Sale::getAmount)
                ));
    }

    public static Map<LocalDate, Double> amountByDateForPeriod(List<Sale> sales, LocalDate from, LocalDate to) {
        validateSales(sales);
        validatePeriod(from, to);
        return filterByPeriod(sales, from, to).stream()
                .collect(Collectors.groupingBy(
                        Sale::getDate,
                        Collectors.summingDouble(Sale::getAmount)
                ));
    }

    private static void validateSales(List<Sale> sales) {
        if (sales == null) {
            throw new IllegalArgumentException("Список продаж не задан");
        }
        for (Sale sale : sales) {
            if (sale == null) {
                throw new IllegalArgumentException("В списке есть null");
            }
        }
    }

    private static void validatePeriod(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException("Некорректный период");
        }
    }
}
