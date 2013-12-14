package com.freakz.hokan_ng.common.service;

import java.util.List;

/**
 * User: petria
 * Date: 12/14/13
 * Time: 9:06 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface TranslateService {

  List<String> translateFiEng(String keyword);

  List<String> translateEngFi(String keyword);

}
