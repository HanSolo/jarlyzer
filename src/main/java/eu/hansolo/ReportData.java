package eu.hansolo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public record ReportData(String jarFilename, LocalDateTime from, LocalDateTime to, String appEnv, Map<ClassItem, List<MethodItem>> classMap) { }
