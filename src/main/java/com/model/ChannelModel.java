package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by haotr on 03/12/2020.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelModel {
    private String nameChannel;
    private String idChannel;
    private String nameVideo;
    private String linkVideo;
}
