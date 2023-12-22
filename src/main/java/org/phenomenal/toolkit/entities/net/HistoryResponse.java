package org.phenomenal.toolkit.entities.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.phenomenal.toolkit.entities.History;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponse {
    List<History> histories;
}
