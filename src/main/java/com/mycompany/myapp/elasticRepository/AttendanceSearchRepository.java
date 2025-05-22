package com.mycompany.myapp.elasticRepository;

import com.mycompany.myapp.domain.Attendance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AttendanceSearchRepository extends ElasticsearchRepository<Attendance, Long> {}
