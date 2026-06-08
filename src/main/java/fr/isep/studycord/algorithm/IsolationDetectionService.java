package fr.isep.studycord.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.isep.studycord.dto.UserActivityVector;
import fr.isep.studycord.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Detects poorly-integrated students with a hand-written, simplified DBSCAN.
 *
 * Each student becomes an activity vector; DBSCAN groups similar students into
 * dense clusters and labels the rest as noise. Noise = isolated student.
 * Complementary to (not a replacement for) {@link BFSService}.
 */
@Service
@RequiredArgsConstructor
public class IsolationDetectionService {

    private static final double DEFAULT_EPS = 0.3;
    private static final int DEFAULT_MIN_PTS = 2;

    private static final int UNCLASSIFIED = 0;
    private static final int NOISE = -1;

    private final UserRepository userRepository;

    public List<UserActivityVector> findIsolatedUsers() {
        return findIsolatedUsers(DEFAULT_EPS, DEFAULT_MIN_PTS);
    }

    /**
     * @param eps    neighbourhood radius in the normalised feature space
     * @param minPts minimum neighbours for a core point
     * @return the students labelled as noise/isolated
     */
    public List<UserActivityVector> findIsolatedUsers(double eps, int minPts) {
        List<UserActivityVector> users = userRepository.computeStudentActivityVectors();
        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        // Normalise so one large-range feature does not dominate the distance.
        double[][] points = normalise(users);
        int[] labels = dbscan(points, eps, minPts);

        List<UserActivityVector> isolated = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (labels[i] == NOISE) {
                isolated.add(users.get(i));
            }
        }
        return isolated;
    }

    /** Min-max rescales every feature column to [0, 1]. */
    private double[][] normalise(List<UserActivityVector> users) {
        int n = users.size();
        int dim = users.get(0).toFeatureArray().length;

        double[][] raw = new double[n][];
        for (int i = 0; i < n; i++) {
            raw[i] = users.get(i).toFeatureArray();
        }

        double[] min = new double[dim];
        double[] max = new double[dim];
        for (int d = 0; d < dim; d++) {
            min[d] = Double.MAX_VALUE;
            max[d] = -Double.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                min[d] = Math.min(min[d], raw[i][d]);
                max[d] = Math.max(max[d], raw[i][d]);
            }
        }

        double[][] points = new double[n][dim];
        for (int i = 0; i < n; i++) {
            for (int d = 0; d < dim; d++) {
                double range = max[d] - min[d];
                points[i][d] = range == 0.0 ? 0.0 : (raw[i][d] - min[d]) / range;
            }
        }
        return points;
    }

    /** Core DBSCAN loop: assigns a cluster id (>= 1) or {@link #NOISE} per point. */
    private int[] dbscan(double[][] points, double eps, int minPts) {
        int n = points.length;
        int[] labels = new int[n];
        int clusterId = 0;

        for (int i = 0; i < n; i++) {
            if (labels[i] != UNCLASSIFIED) {
                continue;
            }
            List<Integer> neighbours = regionQuery(points, i, eps);
            if (neighbours.size() < minPts) {
                labels[i] = NOISE;
            } else {
                clusterId++;
                expandCluster(points, labels, i, neighbours, clusterId, eps, minPts);
            }
        }
        return labels;
    }

    /** Grows a cluster from a core point to all density-reachable points. */
    private void expandCluster(double[][] points, int[] labels, int pointIndex,
            List<Integer> neighbours, int clusterId, double eps, int minPts) {

        labels[pointIndex] = clusterId;

        // The seed list grows as new core points are found, so we iterate by index.
        List<Integer> seeds = new ArrayList<>(neighbours);
        for (int k = 0; k < seeds.size(); k++) {
            int current = seeds.get(k);

            if (labels[current] == NOISE) {
                labels[current] = clusterId; // noise becomes a border point
            }
            if (labels[current] != UNCLASSIFIED) {
                continue;
            }
            labels[current] = clusterId;

            List<Integer> currentNeighbours = regionQuery(points, current, eps);
            if (currentNeighbours.size() >= minPts) {
                seeds.addAll(currentNeighbours);
            }
        }
    }

    /** Indices of all points within {@code eps} of {@code pointIndex} (itself included). */
    private List<Integer> regionQuery(double[][] points, int pointIndex, double eps) {
        List<Integer> neighbours = new ArrayList<>();
        for (int j = 0; j < points.length; j++) {
            if (euclideanDistance(points[pointIndex], points[j]) <= eps) {
                neighbours.add(j);
            }
        }
        return neighbours;
    }

    private double euclideanDistance(double[] a, double[] b) {
        double sum = 0.0;
        for (int d = 0; d < a.length; d++) {
            double diff = a[d] - b[d];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
